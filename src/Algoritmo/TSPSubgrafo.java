package Algoritmo;

import Grafo.GrafoLogistica;
import Modelo.Cidade;
import Modelo.Estrada;

import java.util.*;
import java.util.stream.Collectors;

public class TSPSubgrafo {

    public static List<String> resolucaoTSP(GrafoLogistica grafo, Set<String> nomesCidadesSelecionadas, String cidadeInicial) {

        //Valida e converte as cidades
        Cidade inicio = validarCidade(grafo, cidadeInicial);
        Set<Cidade> cidadesSubgrafo = converterNomesParaCidades(grafo, nomesCidadesSelecionadas);

        if(!cidadesSubgrafo.contains(inicio))
            throw new IllegalArgumentException("A cidade inicial deve estar no conjunto selecionado");


        //Construção do subgrafo
        Map<Cidade, List<Cidade>> subgrafo = construirSubgrafo(grafo, cidadesSubgrafo);

        //Verifica conectividade
        if(!verificarConectividade(subgrafo, inicio))
            throw new IllegalStateException("Não há conexão entre as cidades selecionadas");

        //Resolução do TSP
        List<Cidade> rota = resolverTSPNoSubgrafo(grafo, subgrafo, inicio, cidadesSubgrafo);

        return converterParaNomes(rota);
    }

    private static Map<Cidade, List<Cidade>> construirSubgrafo(GrafoLogistica grafo, Set<Cidade> cidades) {
        Map<Cidade, List<Cidade>> subgrafo = new HashMap<>();

        for(Cidade cidade : cidades) {
            List<Cidade> vizinhos = grafo.getMapaCidades().get(cidade).stream()
                    .map(Estrada::getDestino)
                    .filter(cidades::contains)
                    .collect(Collectors.toList());

            subgrafo.put(cidade, vizinhos);
        }
        return subgrafo;
    }

    private static boolean verificarConectividade(Map<Cidade, List<Cidade>> subgrafo, Cidade inicio) {

        Set<Cidade> visitados = new HashSet<>();
        Deque<Cidade> pilha = new ArrayDeque<>();
        pilha.push(inicio);

        while(!pilha.isEmpty()) {
            Cidade atual = pilha.pop();
            if(visitados.add(atual))
                subgrafo.get(atual).forEach(pilha::push);
        }

        return visitados.size() == subgrafo.size();
    }

    private static List<Cidade> resolverTSPNoSubgrafo(GrafoLogistica grafo, Map<Cidade, List<Cidade>> subgrafo, Cidade inicio, Set<Cidade> cidadesSubgrafo) {

        List<Cidade> rota = new ArrayList<>();
        Set<Cidade> naoVisitadas = new HashSet<>(cidadesSubgrafo);

        rota.add(inicio);
        naoVisitadas.remove(inicio);
        Cidade atual = inicio;

        while(!naoVisitadas.isEmpty()) {
            Cidade proxima = encontrarVizinhoMaisProximo(grafo, subgrafo, atual, naoVisitadas);

            if(proxima == null) {
                proxima = encontrarVizinhoIndireto(grafo, atual, naoVisitadas, cidadesSubgrafo);

                if(proxima == null) {
                    throw new IllegalStateException("Não foi possível encontrar caminho para todas as cidades");
                }
            }

            rota.add(proxima);
            naoVisitadas.remove(proxima);
            atual = proxima;
        }

        if(subgrafo.get(atual).contains(inicio))
            rota.add(inicio);

        return rota;
    }

    private static Cidade encontrarVizinhoMaisProximo(GrafoLogistica grafo, Map<Cidade, List<Cidade>> subgrafo, Cidade atual, Set<Cidade> candidatas) {

        Cidade maisProxima = null;
        double menorDistancia = Double.MAX_VALUE;

        for(Cidade vizinho : subgrafo.get(atual)) {
            if(candidatas.contains(vizinho)) {
                double distancia = grafo.getMapaCidades().get(atual).stream()
                        .filter(e -> e.getDestino().equals(vizinho))
                        .mapToDouble(Estrada::getDistancia)
                        .findFirst()
                        .orElse(Double.MAX_VALUE);

                if(distancia < menorDistancia) {
                    menorDistancia = distancia;
                    maisProxima = vizinho;
                }
            }
        }

        return maisProxima;
    }

    private static Cidade encontrarVizinhoIndireto(GrafoLogistica grafo, Cidade origem, Set<Cidade> candidatas, Set<Cidade> cidadesSubgrafo) {

        PriorityQueue<CidadeDistancia> fila = new PriorityQueue<>();
        Map<Cidade, Double> distancias = new HashMap<>();
        Map<Cidade, Cidade> predecessores = new HashMap<>();

        cidadesSubgrafo.forEach(c -> distancias.put(c, Double.MAX_VALUE));
        distancias.put(origem, 0.0);
        fila.add(new CidadeDistancia(origem, 0.0));

        while(!fila.isEmpty()) {
            Cidade atual = fila.poll().cidade;

            if(candidatas.contains(atual))
                return atual;

            for(Estrada estrada : grafo.getMapaCidades().get(atual)) {
                Cidade vizinho = estrada.getDestino();
                if(!cidadesSubgrafo.contains(vizinho))
                    continue;

                double novaDistancia = distancias.get(atual) + estrada.getDistancia();
                if(novaDistancia < distancias.get(vizinho)) {
                    distancias.put(vizinho, novaDistancia);
                    predecessores.put(vizinho, atual);
                    fila.add(new CidadeDistancia(vizinho, novaDistancia));
                }
            }
        }
        return null;
    }

    //Classes e Métodos auxiliares
    private static class CidadeDistancia implements Comparable<CidadeDistancia> {

        Cidade cidade;
        double distancia;

        public CidadeDistancia(Cidade cidade, double distancia) {
            this.cidade = cidade;
            this.distancia = distancia;
        }

        @Override
        public int compareTo(CidadeDistancia outra) {
            return Double.compare(this.distancia, outra.distancia);
        }
    }

    private static Cidade validarCidade(GrafoLogistica grafo, String nomeCidade) {
        Cidade cidade = grafo.encontrarCidade(nomeCidade);

        if (cidade == null) {
            throw new IllegalArgumentException("Cidade '" + nomeCidade + "' não encontrada");
        }
        return cidade;
    }

    private static Set<Cidade> converterNomesParaCidades(GrafoLogistica grafo, Set<String> nomes) {

        return nomes.stream()
                .map(grafo::encontrarCidade)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private static List<String> converterParaNomes(List<Cidade> cidades) {
        return cidades.stream()
                .map(Cidade::getNome)
                .collect(Collectors.toList());
    }
}
