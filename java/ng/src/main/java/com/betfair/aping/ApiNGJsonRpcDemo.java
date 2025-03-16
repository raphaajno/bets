package com.betfair.aping;

import com.betfair.aping.api.ApiNgJsonRpcOperations;
import com.betfair.aping.api.ApiNgOperations;
import com.betfair.aping.entities.*;
import com.betfair.aping.enums.*;
import com.betfair.aping.exceptions.APINGException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.*;

public class ApiNGJsonRpcDemo {

    private ApiNgOperations jsonOperations = ApiNgJsonRpcOperations.getInstance();
    private String applicationKey;
    private String sessionToken;
    private ResourceBundle bundle;

    // Construtor para definir o locale e carregar a tradução
   public ApiNGJsonRpcDemo() {
        try {
            Locale locale = new Locale("pt", "BR");
            bundle = ResourceBundle.getBundle("messages", locale);
            System.out.println("Bundle carregado com sucesso: " + locale);
        } catch (MissingResourceException e) {
            System.err.println("Erro ao carregar bundle: " + e.getMessage());
        }
    }

    public void start(String appKey, String ssoid) throws APINGException, IOException {
        this.applicationKey = appKey;
        this.sessionToken = ssoid;

        try {
            MarketFilter marketFilter = new MarketFilter();
            Set<String> eventTypeIds = new HashSet<>();
            
            // Exemplo de como gerar o arquivo
            FileWriter fileWriter = new FileWriter("mercados.txt");
            BufferedWriter writer = new BufferedWriter(fileWriter);
            
            System.out.println("1. Obtendo o Event Type Id para Futebol...");
            List<EventTypeResult> eventTypeResults = jsonOperations.listEventTypes(marketFilter, applicationKey, sessionToken);

            for (EventTypeResult eventTypeResult : eventTypeResults) {
                if (eventTypeResult.getEventType().getName().equalsIgnoreCase("Soccer")) {
                    System.out.println("EventTypeId de Futebol encontrado: " + eventTypeResult.getEventType().getId());
                    eventTypeIds.add(eventTypeResult.getEventType().getId().toString());
                    break;
                }
            }

            if (eventTypeIds.isEmpty()) {
                System.out.println("Tipo de evento Futebol não encontrado!");
                return;
            }

            TimeRange time = new TimeRange();
            time.setFrom(new Date());

            Set<String> countries = new HashSet<>();
            countries.add("BR");

            Set<String> typesCode = new HashSet<>();
            typesCode.add("MATCH_ODDS");

            marketFilter.setEventTypeIds(eventTypeIds);
            marketFilter.setMarketStartTime(time);
            marketFilter.setMarketCountries(countries);

            Set<MarketProjection> marketProjection = new HashSet<>();
            marketProjection.add(MarketProjection.RUNNER_DESCRIPTION);
            marketProjection.add(MarketProjection.EVENT);

            String maxResults = "1000";

            List<MarketCatalogue> marketCatalogueResult = jsonOperations.listMarketCatalogue(
                    marketFilter, marketProjection, MarketSort.FIRST_TO_START, maxResults, applicationKey, sessionToken
            );
            
             // Escreve os nomes dos mercados no arquivo
            System.out.println("Gerando lista de mercados...");
            for (MarketCatalogue market : marketCatalogueResult) {
                String marketName = market.getMarketName();
                writer.write(marketName);
                writer.newLine();  // Nova linha para cada nome de mercado
            }

            // Fecha o arquivo depois de escrever
            writer.close();
            System.out.println("Arquivo 'mercados.txt' gerado com sucesso!");

            System.out.println("Listando Partidas de Futebol...");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            // Usando um Map para agrupar por evento (partida)
            Map<String, List<MarketCatalogue>> eventMarkets = new HashMap<>();

            // Agrupa todos os mercados por evento (partida)
            for (MarketCatalogue market : marketCatalogueResult) {
                String eventName = market.getEvent().getName();
                eventMarkets.computeIfAbsent(eventName, k -> new ArrayList<>()).add(market);
            }

            // Agora percorre cada evento e lista todos os seus mercados e runners
            for (Map.Entry<String, List<MarketCatalogue>> entry : eventMarkets.entrySet()) {
                System.out.println("Partida: " + entry.getKey());

                for (MarketCatalogue market : entry.getValue()) {
                    // Aplica a tradução do nome do mercado
                    String marketName = translateMarketName(market.getMarketName());

                    System.out.println("  Mercado: " + marketName
                            + " | Início: " + dateFormat.format(market.getEvent().getOpenDate())
                            + " | Market Id: " + market.getMarketId());

                    printMarketCatalogue(market);
                }
            }
        } catch (APINGException e) {
            System.err.println("Erro na API: " + e.getMessage());
        }
    }

    // Função modificada para listar runners
    private void printMarketCatalogue(MarketCatalogue mk) {
        String marketName = translateMarketName(mk.getMarketName());
        System.out.println("    Nome do Mercado: " + marketName + "; Id: " + mk.getMarketId());
        List<RunnerCatalog> runners = mk.getRunners();
        if (runners != null && !runners.isEmpty()) {
            for (RunnerCatalog rCat : runners) {
                System.out.println("      Nome do Runner: " + rCat.getRunnerName() + "; Handicap: " + rCat.getHandicap() + "; Seleção: " + rCat.getSelectionId());
            }
        } else {
            System.out.println("      Nenhum runner disponível.");
        }
    }

    // Método para traduzir o nome do mercado usando o ResourceBundle
    private String translateMarketName(String marketName) {
        try {
            return bundle.getString(marketName);
        } catch (MissingResourceException e) {
            return marketName; // Se não encontrar tradução, retorna o nome original
        }
    }
}
