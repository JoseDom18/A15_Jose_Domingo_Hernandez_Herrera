package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.event.BlackoutEvent;
import com.axity.dinosaurpark.event.DinosaurEscapeEvent;
import com.axity.dinosaurpark.event.SimulationEvent;
import com.axity.dinosaurpark.event.StormEvent;
import com.axity.dinosaurpark.model.*;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.zone.*;

import java.util.List;
import java.util.Random;

public class SimulationEngine {
    private final ParkState state;
    private final ParkConfig config;
    private final Random rand;
    private final List<SimulationEvent> allEvents;

    public SimulationEngine() {
        this.config = ParkConfig.getInstance();
        this.state = new ParkState();

        long seed = config.getSeed();
        this.rand = new Random(seed);

        this.state.csvWriter = new CsvWriter();

        // Inicializamos los eventos disponibles
        this.allEvents = List.of(new BlackoutEvent(), new DinosaurEscapeEvent(), new StormEvent());

        initializePark();
    }

    private void initializePark() {
        // 1. Zonas
        state.powerPlant = new PowerPlant();
        state.zones.add(new ArrivalZone());
        state.zones.add(new CentralHub());
        state.zones.add(new BathroomZone());
        state.zones.add(new ObservationEnclosure("T-Rex Paddock", ExperienceType.VIP));
        state.zones.add(new ObservationEnclosure("T-Rex Paddock", ExperienceType.BASIC));
        state.zones.add(new ObservationEnclosure("T-Rex Paddock", ExperienceType.PREMIUM));
        state.zones.add(new ObservationEnclosure("Triceratops Field", ExperienceType.BASIC));
        state.zones.add(new ObservationEnclosure("Triceratops Field", ExperienceType.PREMIUM));
        state.zones.add(new ObservationEnclosure("Triceratops Field", ExperienceType.VIP));
        state.zones.add(new ObservationEnclosure("Velociraptor Area", ExperienceType.BASIC));
        state.zones.add(new ObservationEnclosure("Velociraptor Area", ExperienceType.PREMIUM));
        state.zones.add(new ObservationEnclosure("Velociraptor Area", ExperienceType.VIP));

        // 2. Personal
        int guards = config.getInt("workers.guards", 3);
        int techs = config.getInt("workers.technicians", 2);
        double salary = config.getDouble("workers.dailySalary", 150.0);

        for (int i = 0; i < guards; i++) state.workers.add(new Guard("Guard " + (i+1), salary));
        for (int i = 0; i < techs; i++) state.workers.add(new Technician("Tech " + (i+1), salary));

        // 3. Dinosaurios
        int carnivores = config.getInt("dinosaurs.carnivores", 5);
        int herbivores = config.getInt("dinosaurs.herbivores", 15);
        for (int i = 0; i < carnivores; i++) state.dinosaurs.add(new CarnivoreDinosaur("C-Dino " + (i+1), "T-Rex"));
        for (int i = 0; i < herbivores; i++) state.dinosaurs.add(new HerbivoreDinosaur("H-Dino " + (i+1), "Triceratops"));

        // 4. Turistas
        int totalTourists = config.getInt("tourists", 50);
        ArrivalZone arrival = (ArrivalZone) state.zones.get(0);
        for (int i = 0; i < totalTourists; i++) {
            Tourist t = new Tourist("Tourist " + (i+1));
            state.tourists.add(t);
            arrival.enter(t);
        }
    }

    public void run() {
        int totalSteps = config.getTotalSteps();
        System.out.println("Iniciando Simulación del Parque por " + totalSteps + " steps...");

        for (int step = 0; step < totalSteps; step++) {

            ArrivalZone arrival = (ArrivalZone) state.zones.get(0);
            List<Ticket> newTickets = arrival.processBatch(config.getInt("simulation.arrivalBatchSize", 5), state.csvWriter);
            state.soldTickets.addAll(newTickets);

            CentralHub hub = (CentralHub) state.zones.get(1);
            BathroomZone bathroom = (BathroomZone) state.zones.get(2);
            ObservationEnclosure enclosure = (ObservationEnclosure) state.zones.get(3);

            for (Tourist tourist : state.tourists) {
                if (tourist.getStatus() == TouristStatus.IN_PARK) {
                    hub.visit(tourist, rand, state.csvWriter);
                    bathroom.tryEnter(tourist, rand, state.csvWriter);
                    enclosure.enter(tourist);
                    SatisfactionSurvey survey = enclosure.conductSurvey(tourist, rand);
                    state.surveys.add(survey);
                    enclosure.exit(tourist);
                }
            }

            bathroom.tick();
            state.powerPlant.tick(rand, state.csvWriter);

            SimulationEvent randomEvent = allEvents.get(rand.nextInt(allEvents.size()));
            if (rand.nextDouble() < randomEvent.getProbability()) {
                randomEvent.execute(state, rand);
                state.csvWriter.recordEvent(randomEvent.toRecord(step));
            }

            for (Worker worker : state.workers) {
                if (worker instanceof Guard guard) {
                    guard.recapturedEscapedDinosaur(state.dinosaurs);
                } else if (worker instanceof Technician tech) {
                    tech.repairIfNeeded(state.powerPlant, state.csvWriter);
                }
            }
        }

        System.out.println("Simulación finalizada. Revisa la carpeta /output para los reportes CSV.");
    }
}
