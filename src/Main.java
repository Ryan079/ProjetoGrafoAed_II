import Algoritmo.AlgoritmoDijkstra;
import Algoritmo.AlgoritmoVizinhoMaisProximo;
import Algoritmo.TSPSubgrafo;
import Grafo.GrafoLogistica;
import Grafo.LeitorCSV;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);

        GrafoLogistica grafo = new GrafoLogistica();
        grafo = LeitorCSV.carregarGrafo("src/conexoes_pernambuco.csv");

        System.out.println("Mapa de Cidades:");
        grafo.getMapaCidades().forEach((cidade, estradas) -> {
            System.out.println("Cidade: " + cidade.getNome());
            estradas.forEach(e -> System.out.println("  -> " + e.getDestino().getNome() + " (" + e.getDistancia() + ")"));
        });


        while(true) {
            System.out.println("\n--- MENU DO SISTEMA ---");
            System.out.println("1. Exibir cidades");
            System.out.println("2. Exibir menor caminho entre duas cidades");
            System.out.println("3. Percorrer estado.");
            System.out.println("4. Realizar viagem múltipla.");
            System.out.println("0. Sair");
            System.out.println("Escolha uma opção");

            int opcao = input.nextInt();
            input.nextLine();

            switch(opcao) {
                case 1:
                    grafo.exibirMapa();
                    break;
                case 2:
                    System.out.println("Cidade de origem: ");
                    String origem = input.nextLine();
                    System.out.println("Cidade de destino: ");
                    String destino = input.nextLine();
                    AlgoritmoDijkstra.encontrarMenorRota(grafo, origem, destino);
                    break;
                case 3:
                    System.out.println("Cidade inicial: ");
                    String cidade = input.nextLine();
                    try {
                        List<String> rota = AlgoritmoVizinhoMaisProximo.calcularRotaMaisProxima(grafo, cidade);
                        System.out.println("Rota: " + String.join(" -> ", rota));
                    } catch(IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("Insira as cidades das encomendas que precisam ser entregues (digite fim para encerrar)");
                    Set<String> cidadesSelecionadas = new HashSet<>();
                    while(true) {
                        String entrada = input.nextLine().trim();
                        if(entrada.equalsIgnoreCase("fim"))
                            break;
                        if(grafo.encontrarCidade(entrada) != null)
                            cidadesSelecionadas.add(entrada);
                        else
                            System.out.println("Cidade não encontrada. Tente novamente");
                    }
                    if(cidadesSelecionadas.size() < 2) {
                        System.out.println("Adicione pelo menos duas cidades.");
                        return;
                    }

                    System.out.println("Digite a cidade inicial: ");
                    String inicio = input.nextLine().trim();

                    try {
                        List<String> rota = TSPSubgrafo.resolucaoTSP(grafo, cidadesSelecionadas, inicio);

                        System.out.println("\nRota encontrada: ");
                        for(String c : rota)
                            System.out.println(c + " -> ");
                    } catch(Exception e) {
                        System.out.println("Erro ao executar algoritmo: " + e.getMessage());
                    }
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida.");
            }

        }

    }
}