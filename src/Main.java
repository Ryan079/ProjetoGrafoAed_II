import Algoritmo.AlgoritmoDijkstra;
import Grafo.GrafoLogistica;
import Grafo.LeitorCSV;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);

        GrafoLogistica grafo = new GrafoLogistica();
        grafo = LeitorCSV.carregarGrafo("src/conexoes_pernambuco.csv");



        while(true) {
            System.out.println("\n--- MENU DO SISTEMA ---");
            System.out.println("1. Exibir cidades");
            System.out.println("2. Exibir menor caminho entre duas cidades");
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
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida.");
            }

        }

    }
}