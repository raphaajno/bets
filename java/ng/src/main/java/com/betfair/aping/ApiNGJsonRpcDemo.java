package com.betfair.aping;

import com.betfair.aping.api.ApiNgJsonRpcOperations;
import com.betfair.aping.api.ApiNgOperations;
import com.betfair.aping.entities.*;
import com.betfair.aping.enums.*;
import com.betfair.aping.exceptions.APINGException;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.HashMap;
import java.util.Map;

public class ApiNGJsonRpcDemo {
    


private static final Map<String, String> marketTranslations = new HashMap<>();

static {
    marketTranslations.put("Over/Under 2.5 Goals", "Mais/Menos de 2.5 Gols");
    marketTranslations.put("Asian Handicap", "Handicap Asiático");
    marketTranslations.put("Goal Lines", "Linhas de Gol");
    marketTranslations.put("Correct Score", "Placar Exato");
    marketTranslations.put("Over/Under 0.5 Goals", "Mais/Menos de 0.5 Gols");
    marketTranslations.put("Match Odds", "Probabilidades da Partida");
    marketTranslations.put("Both teams to Score?", "Ambos os Times Marcam?");
    marketTranslations.put("Draw no Bet", "Empate Anula Aposta");
    marketTranslations.put("Half Time Score", "Placar do Primeiro Tempo");
    marketTranslations.put("Half Time/Full Time", "Resultado Intervalo/Final");
    marketTranslations.put("Double Chance", "Chance Dupla");
    marketTranslations.put("First Half Goals 1.5", "Gols no Primeiro Tempo 1.5");
    marketTranslations.put("First Half Goals 0.5", "Gols no Primeiro Tempo 0.5");
    marketTranslations.put("First Half Goals 2.5", "Gols no Primeiro Tempo 2.5");
    marketTranslations.put("Half Time", "Intervalo");
}


    private ApiNgOperations jsonOperations = ApiNgJsonRpcOperations.getInstance();
    private String applicationKey;
    private String sessionToken;

    public void start(String appKey, String ssoid) throws APINGException {
        this.applicationKey = appKey;
        this.sessionToken = ssoid;

        try {
            MarketFilter marketFilter = new MarketFilter();
            Set<String> eventTypeIds = new HashSet<>();

            System.out.println("1. Getting Event Type Id for Soccer...");
            List<EventTypeResult> eventTypeResults = jsonOperations.listEventTypes(marketFilter, applicationKey, sessionToken);

            for (EventTypeResult eventTypeResult : eventTypeResults) {
                if (eventTypeResult.getEventType().getName().equalsIgnoreCase("Soccer")) {
                    System.out.println("Soccer EventTypeId found: " + eventTypeResult.getEventType().getId());
                    eventTypeIds.add(eventTypeResult.getEventType().getId().toString());
                    break; // Found Soccer, no need to continue
                }
            }

            if (eventTypeIds.isEmpty()) {
                System.out.println("Soccer event type not found!");
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
            //marketFilter.setMarketTypeCodes(typesCode);

            Set<MarketProjection> marketProjection = new HashSet<>();
            marketProjection.add(MarketProjection.RUNNER_DESCRIPTION);
            marketProjection.add(MarketProjection.EVENT);

            String maxResults = "1000";

            List<MarketCatalogue> marketCatalogueResult = jsonOperations.listMarketCatalogue(
                    marketFilter, marketProjection, MarketSort.FIRST_TO_START, maxResults, applicationKey, sessionToken
            );

            System.out.println("Listing Soccer Matches...");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

// Usando um Map para agrupar por evento (partida)
            Map<String, List<MarketCatalogue>> eventMarkets = new HashMap<>();

// Agrupa todos os mercados por evento (partida)
            for (MarketCatalogue market : marketCatalogueResult) {
                String eventName = market.getEvent().getName();
                eventMarkets.computeIfAbsent(eventName, k -> new ArrayList<>()).add(market);
            }

// Agora, percorre cada evento e lista todos os seus mercados e runners
            for (Map.Entry<String, List<MarketCatalogue>> entry : eventMarkets.entrySet()) {
                System.out.println("Match: " + entry.getKey());

                for (MarketCatalogue market : entry.getValue()) {
                    System.out.println("  Market: " + market.getMarketName()
                            + " | Start Time: " + dateFormat.format(market.getEvent().getOpenDate())
                            + " | Market Id: " + market.getMarketId());

                    printMarketCatalogue(market);
                }
            }
        }
        catch (APINGException e) {
    System.err.println("API Exception: " + e.getMessage());
            // Mostra o stack trace pra diagnóstico
}

    }

// Função modificada para listar runners certinho
    private void printMarketCatalogue(MarketCatalogue mk) {
    // Traduz o nome do mercado, se houver tradução disponível
    String translatedMarketName = marketTranslations.getOrDefault(mk.getMarketName(), mk.getMarketName());

    System.out.println("    Nome do Mercado: " + translatedMarketName + "; Id: " + mk.getMarketId());

    List<RunnerCatalog> runners = mk.getRunners();
    if (runners != null && !runners.isEmpty()) {
        for (RunnerCatalog rCat : runners) {
            System.out.println("      Corredor: " + rCat.getRunnerName() + "; Handicap: " + rCat.getHandicap() + "; Seleção: " + rCat.getSelectionId());
        }
    } else {
        System.out.println("      Nenhum corredor disponível.");
    }
}


}
