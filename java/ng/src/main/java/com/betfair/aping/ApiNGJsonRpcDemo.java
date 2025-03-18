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
    marketTranslations.put("Over/Under 4.5 Goals", "Mais/Menos de 4.5 Gols");
marketTranslations.put("Over/Under 5.5 Goals", "Mais/Menos de 5.5 Gols");
marketTranslations.put("Over/Under 6.5 Goals", "Mais/Menos de 6.5 Gols");
marketTranslations.put("Over/Under 7.5 Goals", "Mais/Menos de 7.5 Gols");
marketTranslations.put("Over/Under 8.5 Goals", "Mais/Menos de 8.5 Gols");
marketTranslations.put("First Half Goals 1.5", "Gols no Primeiro Tempo 1.5");
marketTranslations.put("First Half Goals 2.5", "Gols no Primeiro Tempo 2.5");
marketTranslations.put("First Half Goals 0.5", "Gols no Primeiro Tempo 0.5");
marketTranslations.put("Match Odds", "Probabilidades da Partida");
marketTranslations.put("Correct Score", "Placar Exato");
marketTranslations.put("Draw no Bet", "Empate Anula Aposta");
marketTranslations.put("Double Chance", "Chance Dupla");
marketTranslations.put("Half Time Score", "Placar do Primeiro Tempo");
marketTranslations.put("Half Time/Full Time", "Resultado Intervalo/Final");
marketTranslations.put("Half Time", "Intervalo");
marketTranslations.put("Goal Lines", "Linhas de Gol");

}

private static final Map<String, String> runnerTranslations = new HashMap<>();

static {
    runnerTranslations.put("Under 2.5 Goals", "Menos de 2.5 Gols");
    runnerTranslations.put("Over 2.5 Goals", "Mais de 2.5 Gols");
    runnerTranslations.put("Under 0.5 Goals", "Menos de 0.5 Gols");
    runnerTranslations.put("Over 0.5 Goals", "Mais de 0.5 Gols");
    runnerTranslations.put("Under 1.5 Goals", "Menos de 1.5 Gols");
    runnerTranslations.put("Over 1.5 Goals", "Mais de 1.5 Gols");
    runnerTranslations.put("Under 3.5 Goals", "Menos de 3.5 Gols");
    runnerTranslations.put("Over 3.5 Goals", "Mais de 3.5 Gols");
    runnerTranslations.put("Under 4.5 Goals", "Menos de 4.5 Gols");
    runnerTranslations.put("Over 4.5 Goals", "Mais de 4.5 Gols");
    runnerTranslations.put("Under 5.5 Goals", "Menos de 5.5 Gols");
    runnerTranslations.put("Over 5.5 Goals", "Mais de 5.5 Gols");
    runnerTranslations.put("The Draw", "Empate");
    runnerTranslations.put("Yes", "Sim");
    runnerTranslations.put("No", "Não");
    runnerTranslations.put("Home or Draw", "Casa ou Empate");
    runnerTranslations.put("Draw or Away", "Empate ou Visitante");
    runnerTranslations.put("Home or Away", "Casa ou Visitante");
    runnerTranslations.put("Under 4.5 Goals", "Menos de 4.5 Gols");
runnerTranslations.put("Over 4.5 Goals", "Mais de 4.5 Gols");
runnerTranslations.put("Under 5.5 Goals", "Menos de 5.5 Gols");
runnerTranslations.put("Over 5.5 Goals", "Mais de 5.5 Gols");
runnerTranslations.put("Under 6.5 Goals", "Menos de 6.5 Gols");
runnerTranslations.put("Over 6.5 Goals", "Mais de 6.5 Gols");
runnerTranslations.put("Under 7.5 Goals", "Menos de 7.5 Gols");
runnerTranslations.put("Over 7.5 Goals", "Mais de 7.5 Gols");
runnerTranslations.put("Under 8.5 Goals", "Menos de 8.5 Gols");
runnerTranslations.put("Over 8.5 Goals", "Mais de 8.5 Gols");
runnerTranslations.put("Draw", "Empate");
runnerTranslations.put("Any Other Home Win", "Qualquer Outra Vitória do Mandante");
runnerTranslations.put("Any Other Away Win", "Qualquer Outra Vitória do Visitante");
runnerTranslations.put("Any Other Draw", "Qualquer Outro Empate");
runnerTranslations.put("Any unquoted", "Outro Não Cotado");
runnerTranslations.put("Home or Draw", "Casa ou Empate");
runnerTranslations.put("Draw or Away", "Empate ou Visitante");
runnerTranslations.put("Home or Away", "Casa ou Visitante");
runnerTranslations.put("Yes", "Sim");
runnerTranslations.put("No", "Não");

}

    private ApiNgOperations jsonOperations = ApiNgJsonRpcOperations.getInstance();
    private String applicationKey;
    private String sessionToken;

    public void start(String appKey, String ssoid,String jogo) throws APINGException {
        this.applicationKey = "L6usTYsnqWawE0Vu";
        this.sessionToken = "xStU2HTUZTfnaIMIKym9qofVwXMgzdHYrKBBCK7k5s4=";

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
            //marketFilter.setMarketCountries(countries);
            //marketFilter.setMarketTypeCodes(typesCode);
            marketFilter.setTextQuery(jogo);

            Set<MarketProjection> marketProjection = new HashSet<>();
            marketProjection.add(MarketProjection.RUNNER_DESCRIPTION);
            marketProjection.add(MarketProjection.EVENT);

            String maxResults = "10000";

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
                   // System.out.println("  Market: " + market.getMarketName()
                     //       + " | Start Time: " + dateFormat.format(market.getEvent().getOpenDate())
                       //     + " | Market Id: " + market.getMarketId());

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
           String translatedRunnerName = runnerTranslations.getOrDefault(rCat.getRunnerName(), rCat.getRunnerName());

            System.out.println("      Corredor: " + translatedRunnerName + "; Handicap: " + rCat.getHandicap() + "; Seleção: " + rCat.getSelectionId());
        }
    } else {
        System.out.println("      Nenhum corredor disponível.");
    }
}


}
