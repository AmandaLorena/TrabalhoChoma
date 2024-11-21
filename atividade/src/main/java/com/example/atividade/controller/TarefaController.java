package com.example.atividade.controller;

import com.example.atividade.model.Tarefa;
import com.example.atividade.model.Status;
import com.example.atividade.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    /**
     * Endpoint para criar uma nova tarefa.
     * A tarefa será criada com status "A Fazer" por padrão.
     *
     * @param tarefa Dados da tarefa a ser criada
     * @return Tarefa criada
     */
    @PostMapping
    public ResponseEntity<Tarefa> criarTarefa(@RequestBody Tarefa tarefa) {
        Tarefa novaTarefa = tarefaService.criarTarefa(tarefa);
        return new ResponseEntity<>(novaTarefa, HttpStatus.CREATED);
    }

    /**
     * Endpoint para listar todas as tarefas por status.
     *
     * @param status O status para filtrar as tarefas (A Fazer, Em Progresso, Concluído)
     * @return Lista de tarefas filtradas por status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Tarefa>> listarTarefasPorStatus(@PathVariable Status status) {
        List<Tarefa> tarefas = tarefaService.listarTarefasPorStatus(status);
        return new ResponseEntity<>(tarefas, HttpStatus.OK);
    }

    /**
     * Endpoint para mover uma tarefa para o próximo status (A Fazer → Em Progresso → Concluído).
     *
     * @param id Identificador da tarefa a ser movida
     * @return Tarefa atualizada
     */
    @PutMapping("/{id}/move")
    public ResponseEntity<Tarefa> moverTarefa(@PathVariable Long id) {
        try {
            Tarefa tarefaAtualizada = tarefaService.moverTarefa(id);
            return new ResponseEntity<>(tarefaAtualizada, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint para atualizar os dados de uma tarefa (título, descrição, prioridade, etc.).
     *
     * @param id     Identificador da tarefa a ser atualizada
     * @param tarefa Dados atualizados da tarefa
     * @return Tarefa atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizarTarefa(@PathVariable Long id, @RequestBody Tarefa tarefa) {
        try {
            Tarefa tarefaAtualizada = tarefaService.atualizarTarefa(id, tarefa);
            return new ResponseEntity<>(tarefaAtualizada, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para excluir uma tarefa com base no ID.
     *
     * @param id Identificador da tarefa a ser excluída
     * @return Status da operação
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTarefa(@PathVariable Long id) {
        try {
            tarefaService.excluirTarefa(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para gerar um relatório de tarefas atrasadas.
     *
     * @return Lista de tarefas atrasadas
     */
    @GetMapping("/report")
    public ResponseEntity<List<Tarefa>> gerarRelatorioTarefasAtrasadas() {
        List<Tarefa> tarefasAtrasadas = tarefaService.gerarRelatorioTarefasAtrasadas();
        return new ResponseEntity<>(tarefasAtrasadas, HttpStatus.OK);
    }
}
