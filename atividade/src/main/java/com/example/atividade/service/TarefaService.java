package com.example.atividade.service;

import com.example.atividade.model.Tarefa;
import com.example.atividade.model.Status;
import com.example.atividade.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    /**
     * Cria uma nova tarefa com status inicial "A Fazer" e data de criação automática.
     *
     * @param tarefa Objeto da tarefa a ser salva
     * @return Tarefa salva
     */
    public Tarefa criarTarefa(Tarefa tarefa) {
        tarefa.setStatus(Status.A_FAZER); // Define o status inicial
        tarefa.setDataCriacao(LocalDate.now()); // Define a data de criação atual
        return tarefaRepository.save(tarefa);
    }

    /**
     * Lista todas as tarefas organizadas por coluna (status).
     *
     * @return Lista de tarefas
     */
    public List<Tarefa> listarTarefasPorStatus(Status status) {
        return tarefaRepository.findByStatusOrderByPrioridadeAsc(status);
    }

    /**
     * Move uma tarefa para o próximo status com base na regra de negócio.
     *
     * @param id Identificador da tarefa a ser movida
     * @return Tarefa atualizada com o próximo status
     * @throws IllegalArgumentException Se a tarefa já estiver no status "Concluído"
     */
    public Tarefa moverTarefa(Long id) {
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);

        if (tarefaOptional.isEmpty()) {
            throw new IllegalArgumentException("Tarefa não encontrada com o ID fornecido.");
        }

        Tarefa tarefa = tarefaOptional.get();
        switch (tarefa.getStatus()) {
            case A_FAZER -> tarefa.setStatus(Status.EM_PROGRESSO); // A Fazer → Em Progresso
            case EM_PROGRESSO -> tarefa.setStatus(Status.CONCLUIDO); // Em Progresso → Concluído
            case CONCLUIDO -> throw new IllegalArgumentException("Tarefa já está no status 'Concluído'.");
        }

        return tarefaRepository.save(tarefa);
    }

    /**
     * Atualiza as informações de uma tarefa existente.
     *
     * @param id     Identificador da tarefa a ser atualizada
     * @param tarefa Dados atualizados da tarefa
     * @return Tarefa atualizada
     */
    public Tarefa atualizarTarefa(Long id, Tarefa tarefa) {
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);

        if (tarefaOptional.isEmpty()) {
            throw new IllegalArgumentException("Tarefa não encontrada com o ID fornecido.");
        }

        Tarefa tarefaExistente = tarefaOptional.get();
        tarefaExistente.setTitulo(tarefa.getTitulo());
        tarefaExistente.setDescricao(tarefa.getDescricao());
        tarefaExistente.setPrioridade(tarefa.getPrioridade());
        tarefaExistente.setDataLimite(tarefa.getDataLimite());

        return tarefaRepository.save(tarefaExistente);
    }

    /**
     * Exclui uma tarefa com base no ID fornecido.
     *
     * @param id Identificador da tarefa a ser excluída
     */
    public void excluirTarefa(Long id) {
        if (!tarefaRepository.existsById(id)) {
            throw new IllegalArgumentException("Tarefa não encontrada com o ID fornecido.");
        }
        tarefaRepository.deleteById(id);
    }

    /**
     * Gera um relatório destacando tarefas atrasadas.
     *
     * @return Lista de tarefas atrasadas (status não concluído e data limite passada)
     */
    public List<Tarefa> gerarRelatorioTarefasAtrasadas() {
        return tarefaRepository.findByStatusNotAndDataLimiteBefore(Status.CONCLUIDO, LocalDate.now());
    }
}
