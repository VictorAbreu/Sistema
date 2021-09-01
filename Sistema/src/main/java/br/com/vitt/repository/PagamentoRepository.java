package br.com.vitt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.vitt.model.Pagamento;

@Repository
@Transactional
public interface PagamentoRepository  extends CrudRepository<Pagamento, Long>{
	
	@Query("select p from Pagamento p where p.cliente.id = ?1")
	public List<Pagamento> getPagamentos(Long clienteid);

}
