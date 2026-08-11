import "./App.css";

import { TerrainLegend } from "./features/world/components/TerrainLegend";
import { WorldMap } from "./features/world/components/WorldMap";

import { useSimulation } from "./features/world/hooks/useSimulation";
import { getProductionQuantity } from "./features/world/utils/resources";

const WORLD_WIDTH = 80;
const WORLD_HEIGHT = 48;

function App() {
  const {
    simulation,
    error: simulationError,
    generateSimulation,
    advanceTurn,
  } = useSimulation({
    width: WORLD_WIDTH,
    height: WORLD_HEIGHT,
  });

  const world = simulation?.world ?? null;

  return (
      <main className="application">
        <header className="application-header">
          <div>
            <h1>Chronicles</h1>
            <p>Simulateur de civilisation</p>
          </div>

          <div className="application-actions">
            <button
                type="button"
                onClick={() => void generateSimulation()}
            >
              Générer un nouveau monde
            </button>

            <button
                type="button"
                onClick={() => void advanceTurn()}
                disabled={!simulation}
            >
              Passer un tour
            </button>
          </div>
        </header>

        {simulationError && (
            <section className="message message-error">
              <strong>
                Impossible de créer la simulation
              </strong>

              <p>{simulationError}</p>
            </section>
        )}

        {!world && !simulationError && (
            <section className="message">
              <p>Génération du monde en cours...</p>
            </section>
        )}

        {world && (
            <section className="world-panel">
              <div className="world-panel-header">
                <div>
                  <h2>Monde généré</h2>

                  <p>
                    {world.width} x {world.height} cases
                  </p>
                </div>

                <dl className="world-statistics">
                  <div>
                    <dt>Largeur</dt>
                    <dd>{world.width}</dd>
                  </div>

                  <div>
                    <dt>Hauteur</dt>
                    <dd>{world.height}</dd>
                  </div>

                  <div>
                    <dt>Cases</dt>
                    <dd>{world.tiles.length}</dd>
                  </div>

                  <div>
                    <dt>Tour</dt>
                    <dd>{simulation?.turn ?? "-"}</dd>
                  </div>
                </dl>
              </div>

              <div className="world-map-container">
                <WorldMap world={world} />
              </div>

              <TerrainLegend world={world} />

              <section className="civilization-list">
                <h3>Civilisations</h3>

                <ul>
                  {world.civilizations.map((civilization) => {
                    const territory =
                        world.territories.find(
                            (candidate) =>
                                candidate.civilizationId
                                === civilization.id,
                        );

                    const civilizationState =
                        simulation?.civilizationStates.find(
                            (state) =>
                                state.civilizationId
                                === civilization.id,
                        );

                    const population =
                        civilizationState?.population;

                    const production =
                        world.territoryProductions.find(
                            (candidate) =>
                                candidate.civilizationId
                                === civilization.id,
                        );

                    const foodProduction =
                        getProductionQuantity(
                            production,
                            "FOOD",
                        );

                    const woodProduction =
                        getProductionQuantity(
                            production,
                            "WOOD",
                        );

                    const stoneProduction =
                        getProductionQuantity(
                            production,
                            "STONE",
                        );

                    const oreProduction =
                        getProductionQuantity(
                            production,
                            "ORE",
                        );

                    const foodConsumption =
                        population?.foodConsumptionPerTurn ?? 0;

                    const foodBalance =
                        foodProduction - foodConsumption;

                    const stock =
                        civilizationState?.stock;

                    const foodStock =
                        stock?.values.FOOD ?? 0;

                    const woodStock =
                        stock?.values.WOOD ?? 0;

                    const stoneStock =
                        stock?.values.STONE ?? 0;

                    const oreStock =
                        stock?.values.ORE ?? 0;

                    return (
                      <li key={civilization.id}>
                        <span
                          className="civilization-color"
                          style={{
                            backgroundColor:
                            civilization.color,
                          }}
                        />

                        <span className="civilization-details">
                          <span className="civilization-name">
                            <strong>
                              {civilization.name}
                            </strong>
                            {" — "}
                            {civilization.capital.name}
                          </span>

                          <span>
                            {(population?.inhabitants ?? 0)
                              .toLocaleString("fr-FR")}
                            {" habitants"}
                            {" — "}
                            {territory?.positions.length ?? 0}
                            {" cases"}
                          </span>

                          <span className="civilization-statistics">
                            Production :
                            {" "}
                            🍞 {foodProduction}
                            {" · "}
                            🪵 {woodProduction}
                            {" · "}
                            🪨 {stoneProduction}
                            {" · "}
                            ⛏️ {oreProduction}
                          </span>

                          <span className="civilization-statistics">
                            Stocks :
                            {" "}
                            🍞 {foodStock}
                            {" · "}
                            🪵 {woodStock}
                            {" · "}
                            🪨 {stoneStock}
                            {" · "}
                            ⛏️ {oreStock}
                          </span>

                          <span className="civilization-statistics">
                            Consommation :
                            {" "}
                            {foodConsumption}
                            {" nourriture / tour"}
                          </span>

                          <span
                            className={[
                              "food-balance",
                              foodBalance >= 0
                                ? "food-balance--positive"
                                : "food-balance--negative",
                            ].join(" ")}
                          >
                            Solde alimentaire :
                            {" "}
                            {foodBalance >= 0 ? "+" : ""}
                            {foodBalance}
                          </span>
                        </span>
                      </li>
                    );
                  })}
                </ul>
              </section>
            </section>
        )}
      </main>
  );
}

export default App;