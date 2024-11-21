package com.example.atividade.repository;

import com.example.atividade.model.Tarefa;
import com.example.atividade.model.Status;
import com.example.atividade.model.Prioridade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    // 1. Ordenar tarefas por prioridade dentro de cada coluna
    List<Tarefa> findByStatusOrderByPrioridadeAsc(Status status);

    // 2. Filtrar tarefas por prioridade
    List<Tarefa> findByPrioridade(Prioridade prioridade);

    // 3. Filtrar tarefas por data limite
    List<Tarefa> findByDataLimite(LocalDate dataLimite);

    // 4. Relatório de tarefas atrasadas (não concluídas e data limite ultrapassada)
    List<Tarefa> findByStatusNotAndDataLimiteBefore(Status status, LocalDate dataLimite);
}
