package com.betfair.aping;

import com.betfair.aping.api.ApiNgJsonRpcOperations;
import com.betfair.aping.api.ApiNgOperations;
import com.betfair.aping.entities.*;
import com.betfair.aping.enums.*;
import com.betfair.aping.exceptions.APINGException;

import java.text.SimpleDateFormat;
import java.util.*;

public class ApiNGJsonRpcDemo {

    private ApiNgOperations jsonOperations = ApiNgJsonRpcOperations.getInstance();
    private String applicationKey;
    private String sessionToken;

    public void start(String appKey, String ssoid) {
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
          //  marketFilter.setMarketCountries(countries);
            marketFilter.setMarketTypeCodes(typesCode);

            Set<MarketProjection> marketProjection = new HashSet<>();
            marketProjection.add(MarketProjection.RUNNER_DESCRIPTION);
            marketProjection.add(MarketProjection.EVENT);

            String maxResults = "10";

            List<MarketCatalogue> marketCatalogueResult = jsonOperations.listMarketCatalogue(
                    marketFilter, marketProjection, MarketSort.FIRST_TO_START, maxResults, applicationKey, sessionToken
            );

            System.out.println("Listing Soccer Matches...");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            for (MarketCatalogue market : marketCatalogueResult) {
                System.out.println("Match: " + market.getEvent().getName() + 
                        " | Market: " + market.getMarketName() +
                        " | Start Time: " + dateFormat.format(market.getEvent().getOpenDate()) +
                        " | Market Id: " + market.getMarketId());
                //printMarketCatalogue(market);
            }

        } catch (APINGException apiExc) {
            apiExc.printStackTrace();
        }
    }

    private void printMarketCatalogue(MarketCatalogue mk) {
        System.out.println("Market Name: " + mk.getMarketName() + "; Id: " + mk.getMarketId());
        List<RunnerCatalog> runners = mk.getRunners();
        if (runners != null) {
            for (RunnerCatalog rCat : runners) {
                System.out.println("Runner Name: " + rCat.getRunnerName() + "; Selection Id: " + rCat.getSelectionId());
            }
        }
    }
} 
