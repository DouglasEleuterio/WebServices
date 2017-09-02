package br.com.devmedia.webservice.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.devmedia.webservice.exceptions.DAOException;
import br.com.devmedia.webservice.exceptions.ErrorCode;
import br.com.devmedia.webservice.model.domain.Produto;

public class ProdutoDAO {

	private String selectAll = "select p from Produto p";
	// private String getById = "select p from Produto p";

	/*
	 * Metodo responsável por devolver a lista de objetos.
	 */
	public java.util.List<Produto> getAll() {
		EntityManager em = JPAUtil.geEntityManager();
		List<Produto> produtos = null;

		try {
			produtos = em.createQuery(selectAll, Produto.class).getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao recuperar todos os produtos do banco:" + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		return produtos;
	}

	/*
	 * Metodo responsável por devolver o objeto passado o Id como parametro.
	 */
	public Produto getById(long id) {
		EntityManager em = JPAUtil.geEntityManager();
		Produto produto = null;

		if (id <= 0) {
			throw new DAOException("O id precisa ser maior do que 0", ErrorCode.BAD_REQUEST.getCode());
		}

		try {
			produto = em.find(Produto.class, id);
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar o produto por id no banco de dados: " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());

		} finally {
			em.close();
		}

		if (produto == null) {
			throw new DAOException("Produto de id " + id + " não existe.", ErrorCode.NOT_FOUND.getCode());
		}

		return produto;
	}

	/*
	 * Metodo responsável por salvar os Objetos.
	 */
	public Produto save(Produto produto) {
		EntityManager em = JPAUtil.geEntityManager();

		if(!produtoIsValid(produto)) {
			throw new DAOException("Produto com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
		}
		
		
		try {
			em.getTransaction().begin();
			em.persist(produto);
			em.getTransaction().commit();
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
				throw new DAOException("Erro ao salvar produto no banco de dados: "+ ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
			
		} finally {
			em.close();
		}
		return produto;
	}

	/*
	 * Metodo responsável por Atualizar o produto salvo no banco.
	 */

	public Produto update(Produto produto) {
		EntityManager em = JPAUtil.geEntityManager();
		Produto produtoManaged = null;

		if (produto.getId() <= 0) {
			throw new DAOException("O id precisa ser meior do que 0.", ErrorCode.BAD_REQUEST.getCode());
		}

		if (!produtoIsValid(produto)) {
			throw new DAOException("Produto com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
		}

		try {
			em.getTransaction().begin();
			produtoManaged = em.find(Produto.class, produto.getId());// Passamos para buscar o produto por meio da sua
																		// classe e seu id para localização.

			produtoManaged.setNome(produto.getNome());// Leia assim, Set nome. Aonde? Produto.getNome
			produtoManaged.setQuantidade(produto.getQuantidade());
			em.getTransaction().commit();
		} catch (NullPointerException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Produto informado para atualização não existe" + ex.getMessage(),
					ErrorCode.NOT_FOUND.getCode());
		} catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Erro ao atualizar produto no banco de dados" + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}

		return produtoManaged;
	}

	/*
	 * Metodo responsável por Deletar o produto do banco de dados, e ainda retornar
	 * o produto excluido.
	 */
	public Produto delete(Long id) {
		EntityManager em = JPAUtil.geEntityManager();
		Produto produto = null;

		if (id <= 0) {
			throw new DAOException("O id precisa ser maior que 0.", ErrorCode.BAD_REQUEST.getCode());
		}

		try {
			em.getTransaction().begin();
			produto = em.find(Produto.class, id);
			em.remove(produto);
			em.getTransaction().commit();
		} catch (IllegalArgumentException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Produto informado para remoção não existe" + ex.getMessage(),
					ErrorCode.NOT_FOUND.getCode());
		} catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DAOException("Erro ao remover o produto do banco de dados:" + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}

		return produto;
	}

	private boolean produtoIsValid(Produto produto) {
		try {
			if ((produto.getNome().isEmpty()) || (produto.getQuantidade() <= 0)) {
				return false;
			}
		} catch (NullPointerException ex) {
			throw new DAOException("Produtos com dados imcompletos.", ErrorCode.BAD_REQUEST.getCode());
		}
		return true;
	}

	public List<Produto> getByPaginator(int firtsResult, int maxResult) {
		List<Produto> produtos;
		EntityManager em = JPAUtil.geEntityManager();

		try {
			produtos = em.createQuery(selectAll, Produto.class).setFirstResult(firtsResult - 1).setMaxResults(maxResult)
					.getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar produtos no banco de dados" + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}

		if (produtos.isEmpty()) {
			throw new DAOException("Página com produtos vazia", ErrorCode.NOT_FOUND.getCode());
		}
		return produtos;
	}

	public List<Produto> getByName(String name) {
		EntityManager em = JPAUtil.geEntityManager();
		List<Produto> produtos = null;

		try {
			produtos = em.createQuery("select p from Produto p where p.nome like :name", Produto.class)
					.setParameter("name", "%" + name + "%").getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar produtos por nome no banco de dados: " + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}

		if (produtos.isEmpty()) {
			throw new DAOException("A consulta não retornou elementos.", ErrorCode.NOT_FOUND.getCode());
		}

		return produtos;
	}

	public List<Produto> getByPagination(int firstResult, int maxResults) {
		List<Produto> produtos;
		EntityManager em = JPAUtil.geEntityManager();
		 		
		try {
			produtos = em.createQuery("select p from Produto p", Produto.class)
					.setFirstResult(firstResult - 1)
					.setMaxResults(maxResults)
					.getResultList();
		} catch (RuntimeException ex) {
			throw new DAOException("Erro ao buscar produtos no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		} finally {
			em.close();
		}
		
		if (produtos.isEmpty()) {
			throw new DAOException("Página com produtos vazia.", ErrorCode.NOT_FOUND.getCode());
		}
		
		return produtos;
	}
	
	
}
