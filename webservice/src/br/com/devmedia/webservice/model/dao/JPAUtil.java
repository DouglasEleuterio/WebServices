package br.com.devmedia.webservice.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Douglas
 * Por se tratar de um Objeto "Pesado" e lento, responsável por várias tarefas como:
 * Ler o XML
 * Estabelecer Conexão com o BANCO
 * Identificar as Entidades
 * 
 * Leva-se tempo para inicialização.
 */
		

public class JPAUtil {

	private static EntityManagerFactory emf;
	// Privado e estatico garantiremos que teremos apenas este atributo durante toda
	// a aplicação;

	public static EntityManager geEntityManager() { // Publico para que podemos acessar de qualquer local da aplicação,
													// e estatico para que não precisamos instanciar.

		if (emf == null) { // Verifica se já foi iniciado
			emf = Persistence.createEntityManagerFactory("produtos"); // Passamos a unidade de persistencia definida no
																		// persistence.xml como parametro
		}
		return emf.createEntityManager();
	}

}
