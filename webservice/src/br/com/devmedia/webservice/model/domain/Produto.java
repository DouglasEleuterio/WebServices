package br.com.devmedia.webservice.model.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // A classe é uma entidade
public class Produto {

	@Id // Identificador Unico
	@GeneratedValue(strategy=GenerationType.IDENTITY) // Geração de Identidade, auto incremento MySQL
	private Long id;
	
	private String nome;
	private int quantidade;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

}
