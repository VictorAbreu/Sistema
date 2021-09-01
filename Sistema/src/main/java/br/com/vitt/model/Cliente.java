package br.com.vitt.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Cliente  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@NotNull(message="Nome não pode ser nulo!")
	@NotEmpty(message="Nome não pode ser vazio!")
	private String nome;
	private String email;
	
	@OneToMany(mappedBy="cliente", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Pagamento> pagamento;
	
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<Pagamento> getPagamento() {
		return pagamento;
	}
	public void setPagamento(List<Pagamento> pagamento) {
		this.pagamento = pagamento;
	}
	
	
	

}
