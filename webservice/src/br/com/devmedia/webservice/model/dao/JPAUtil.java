package br.com.devmedia.webservice.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Douglas
 * Por se tratar de um Objeto "Pesado" e lento, respons�vel por v�rias tarefas como:
 * Ler o XML
 * Estabelecer Conex�o com o BANCO
 * Identificar as Entidades
 * 
 * Leva-se tempo para inicializa��o.
 */
		

public class JPAUtil {

	private static EntityManagerFactory emf;
	// Privado e estatico garantiremos que teremos apenas este atributo durante toda
	// a aplica��o;

	public static EntityManager geEntityManager() { // Publico para que podemos acessar de qualquer local da aplica��o,
													// e estatico para que n�o precisamos instanciar.

		if (emf == null) { // Verifica se j� foi iniciado
			emf = Persistence.createEntityManagerFactory("produtos"); // Passamos a unidade de persistencia definida no
																		// persistence.xml como parametro
		}
		return emf.createEntityManager();
	}

}
