import "./App.css";

import { useWorld } from "./features/world/hooks/useWorld";

const WORLD_WIDTH = 20;
const WORLD_HEIGHT = 12;

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
        <section className="world-summary">
          <h2>Monde généré</h2>

          <dl>
            <div>
              <dt>Largeur</dt>
              <dd>{world.width}</dd>
            </div>

            <div>
              <dt>Hauteur</dt>
              <dd>{world.height}</dd>
            </div>

            <div>
              <dt>Nombre de cases</dt>
              <dd>{world.tiles.length}</dd>
            </div>
          </dl>

          <h3>Premières cases</h3>

          <ul className="tile-list">
            {world.tiles.slice(0, 10).map((tile) => (
                <li
                  key={`${tile.position.x}-${tile.position.y}`}
                >
                  <span>
                    ({tile.position.x}, {tile.position.y})
                  </span>

                  <strong>{tile.terrainType}</strong>
                </li>
            ))}
          </ul>
        </section>
      )}
    </main>
  );
}

export default App;