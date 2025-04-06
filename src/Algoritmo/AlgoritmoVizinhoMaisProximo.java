package Algoritmo;

import Grafo.GrafoLogistica;
import Modelo.Cidade;
import Modelo.Estrada;

import java.util.*;
import java.util.stream.Collectors;

public class AlgoritmoVizinhoMaisProximo {

    //Função principal do algoritmo
    public static List<String> calcularRotaMaisProxima(GrafoLogistica grafo, String cidadeInicial) {

        //Busca pela cidade inicial
        Cidade inicial = grafo.encontrarCidade(cidadeInicial);

        if(inicial == null)
            throw new IllegalArgumentException("Cidade inicial não encontrada.");

        //Listas de controle pro algoritmo
        List<Cidade> viagem = new ArrayList<>();
        List<Cidade> cidadesNaoVisitadas = new ArrayList<>(grafo.getMapaCidades().keySet());

        //Inicializa a viagem
        viagem.add(inicial);
        cidadesNaoVisitadas.remove(inicial);
        Cidade atual = inicial;

        //Algoritmo
        while(!cidadesNaoVisitadas.isEmpty()) {
            Cidade proxima = encontrarVizinhoAcessivel(grafo, atual, cidadesNaoVisitadas, viagem);

            viagem.add(proxima);
            if(cidadesNaoVisitadas.contains(proxima))
                cidadesNaoVisitadas.remove(proxima);
            atual = proxima;
        }

        if(existeConexao(grafo, atual, inicial))
            viagem.add(inicial);

        viagem.add(inicial);

        return viagem.stream()
                .map(Cidade::getNome)
                .collect(Collectors.toList());
    }

    private static Cidade encontrarVizinhoAcessivel(GrafoLogistica grafo, Cidade atual, List<Cidade> naoVisitadas, List<Cidade> viagem) {

        //Caso 1 - Tenta encontrar vizinho direto não visitado
        Cidade vizinhoDireto = encontrarVizinhoDiretoMaisProximo(grafo, atual, naoVisitadas);
        if(vizinhoDireto != null)
            return vizinhoDireto;

        //Caso 2 - Procura caminho mais curto para qualquer cidade não visitada
        for(Cidade destino : naoVisitadas){
            Cidade intermediaria = encontrarCidadeIntermediaria(grafo, atual, destino, viagem);
            if(intermediaria != null)
                return intermediaria;
        }

        return viagem.get(0);
    }

    //Algoritmo Caso 1
    private static Cidade encontrarVizinhoDiretoMaisProximo(GrafoLogistica grafo, Cidade atual, List<Cidade> candidatas) {
        Cidade maisProxima = null;
        double menorDistancia = Double.MAX_VALUE;

        for(Estrada estrada : grafo.getMapaCidades().get(atual)) {
            if(candidatas.contains(estrada.getDestino()) && estrada.getDistancia() < menorDistancia) {
                maisProxima = estrada.getDestino();
                menorDistancia = estrada.getDistancia();
            }
        }

        return maisProxima;
    }

    //Algoritmo Caso 2
    private static Cidade encontrarCidadeIntermediaria(GrafoLogistica grafo, Cidade origem, Cidade destino, List<Cidade> viagem) {

        for(Estrada estrada : grafo.getMapaCidades().get(origem)) {
            if(!viagem.contains(estrada.getDestino()) || estrada.getDestino().equals(destino))
                return estrada.getDestino();
        }
        return null;
    }

    private static boolean existeConexao(GrafoLogistica grafo, Cidade origem, Cidade destino) {
        for(Estrada estrada : grafo.getMapaCidades().get(origem)) {
            if(estrada.getDestino().equals(destino))
                return true;
        }
        return false;
    }
}
