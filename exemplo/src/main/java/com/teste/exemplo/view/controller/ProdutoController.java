package com.teste.exemplo.view.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import com.teste.exemplo.services.ProdutoService;
import com.teste.exemplo.shared.ProdutoDTO;
import com.teste.exemplo.view.model.ProdutoRequest;
import com.teste.exemplo.view.model.ProdutoResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    
    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> obterTodos(){
        List<ProdutoDTO> produtos = produtoService.obterTodos();
        ModelMapper mapper = new ModelMapper();
        List<ProdutoResponse> resposta = produtos.stream().map(produto->mapper.map(produto,ProdutoResponse.class)).collect(Collectors.toList());
        return new ResponseEntity<>(resposta,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ProdutoResponse>> obterPorId(@PathVariable Integer id){
        //try {
            Optional<ProdutoDTO> dto = produtoService.obterPorId(id);
            ProdutoResponse produto = new ModelMapper().map(dto.get(),ProdutoResponse.class);
            return new ResponseEntity<>(Optional.of(produto),HttpStatus.OK);
        //} catch (Exception e) {
        //    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        //}
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> adicionar(@RequestBody ProdutoRequest produtoReq){
        ModelMapper mapper = new ModelMapper();
        ProdutoDTO dto = mapper.map(produtoReq, ProdutoDTO.class);
        dto = produtoService.adicionar(dto);
        return new ResponseEntity<>(mapper.map(dto, ProdutoResponse.class),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarPorId(@PathVariable Integer id){
        produtoService.deletar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@RequestBody ProdutoRequest produtoReq,@PathVariable Integer id){
        ModelMapper mapper = new ModelMapper();
        ProdutoDTO dto = produtoService.atualizar(id, mapper.map(produtoReq, ProdutoDTO.class));
        return new ResponseEntity<>(mapper.map(dto, ProdutoResponse.class),HttpStatus.OK);
    }

}
