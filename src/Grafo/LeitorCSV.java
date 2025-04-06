package Grafo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeitorCSV {
    public static GrafoLogistica carregarGrafo(String caminhoArquivo) throws IOException {
        GrafoLogistica grafo = new GrafoLogistica();

        try(BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            boolean cabecalho = true;

            while((linha = br.readLine()) != null) {
                if(cabecalho) {
                    cabecalho = false;
                    continue;
                }

                String[] dados = linha.split(",");
                if(dados.length >= 3) {
                    String origem = dados[0].trim();
                    String destino = dados[1].trim();
                    double distancia = Double.parseDouble(dados[2].trim());

                    grafo.adicionarCidade(origem);
                    grafo.adicionarCidade(destino);

                    grafo.adicionarEstrada(origem, destino, distancia);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        return grafo;
    }
}
