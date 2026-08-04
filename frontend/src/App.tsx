import "./App.css";

import { useWorld } from "./features/world/hooks/useWorld";

import { TerrainLegend } from "./features/world/components/TerrainLegend";
import { WorldMap } from "./features/world/components/WorldMap";

const WORLD_WIDTH = 40;
const WORLD_HEIGHT = 24;

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
        </section>
      )}
    </main>
  );
}

export default App;