import "./App.css";

import { useWorld } from "./features/world/hooks/useWorld";

import { TerrainLegend } from "./features/world/components/TerrainLegend";
import { WorldMap } from "./features/world/components/WorldMap";

const WORLD_WIDTH = 80
const WORLD_HEIGHT = 48;

function App() {
  const {
    world,
    isLoading,
    error,
    regenerate,
  } = useWorld({
    width: WORLD_WIDTH,
    height: WORLD_HEIGHT,
  });

  return (
    <main className="application">
      <header className="application-header">
        <div>
          <h1>Chronicles</h1>
          <p>Simulateur de civilisation</p>
        </div>

        <button
            type="button"
            onClick={() => void regenerate()}
            disabled={isLoading}
        >
          {isLoading
              ? "Génération..."
              : "Générer un nouveau monde"
          }
        </button>
      </header>

      {error && (
        <section className="message message-error">
          <strong>Impossible de générer le monde</strong>
          <p>{error}</p>
        </section>
      )}

      {isLoading && !world && (
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
                const territory = world.territories.find(
                  (candidate) =>
                    candidate.civilizationId
                      === civilization.id
                );

                return (
                  <li key={civilization.id}>
                    <span
                      className="civilization-color"
                      style={{
                        backgroundColor: civilization.color,
                      }}
                    />

                    <span className="civilization-detail">
                      <strong>{civilization.name}</strong>

                      <span>
                        {civilization.capital.name}
                        {" — "}
                        {civilization.capital.population.inhabitants.toLocaleString("fr-FR")}
                        {" habitants"}
                        {" | "}
                      </span>

                      <span className="civilization-statistics">
                        Croissance potentielle :{" "}
                        {
                          (civilization.capital.population.growthRate * 100).toLocaleString("fr-FR", {
                            maximumFractionDigits: 1,
                          })
                        }
                        {" %"}
                        {" — "}
                        Nourriture requise :{" "}
                        {
                          civilization.capital.population
                            .foodConsumptionPerTurn
                        }
                        {" / tour"}
                        {" — "}
                        {territory?.positions.length ?? 0}
                        {" cases"}
                      </span>
                    </span>
                  </li>
                )
              })}
            </ul>
          </section>

        </section>
      )}
    </main>
  );
}

export default App;