package Grafo;

import Modelo.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrafoLogistica {
    private Map<Cidade, List<Estrada>> mapaCidades;

    public GrafoLogistica() {
        this.mapaCidades = new HashMap<>();
    }

    public void adicionarCidade(String nome) {
        Cidade c = new Cidade(nome);
        mapaCidades.putIfAbsent(c, new ArrayList<>());
    }

    public void adicionarEstrada(String nomeOrigem, String nomeDestino, double distancia) {
        Cidade origem = encontrarCidade(nomeOrigem);
        Cidade destino = encontrarCidade(nomeDestino);

        if(origem != null && destino != null) {
            //Trecho feito para gerar conectividade A -> B e B -> A
            Estrada A = new Estrada(origem, destino, distancia);
            Estrada B = new Estrada(destino, origem, distancia);
            mapaCidades.get(origem).add(A);
            mapaCidades.get(destino).add(B);
        }
    }

    public Map<Cidade, List<Estrada>> getMapaCidades() {
        return mapaCidades;
    }

    public void exibirMapa() {
        for(Cidade c : mapaCidades.keySet()){
            System.out.println(c.getNome() + " -> ");
            List<Estrada> adjacentes = mapaCidades.get(c);
            System.out.println(adjacentes.stream()
                    .map(e -> e.getDestino().getNome() + " (" + e.getDistancia() + " km)")
                    .toList());
        }
    }

    private Cidade encontrarCidade(String nome) {
        return mapaCidades.keySet().stream()
                .filter(cidade -> cidade.getNome().equals(nome))
                .findFirst()
                .orElse(null);
    }

}
