import { useEffect, useState } from "react";
import "./App.css";

type SystemStatus = {
  application: string;
  status: string;
  timestamp: string;
};

function App() {
  const [systemStatus, setSystemStatus] = useState<SystemStatus | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadSystemStatus = async () => {
      try {
        const apiUri = import.meta.env.VITE_API_URI ?? "";

        const response = await fetch(`${apiUri}/api/system/status`);

        if (!response.ok) {
          throw new Error(`Erreur HTTP ${response.status}`);
        }

        const data: SystemStatus = await response.json();
        setSystemStatus(data);
      } catch (requestError) {
        const message =
          requestError instanceof Error
            ? requestError.message
            : "Une erreur inconnue est survenue";
      }
    };

    void loadSystemStatus();
  }, []);


  return (
      <main className="appication">
        <section className="status-card">
          <h1>Chronicles</h1>

          <p>Simulateur de civilisation</p>

          {systemStatus && (
              <div className="success">
                <strong>Backend connecté</strong>

                <dl>
                  <div>
                    <dt>Application</dt>
                    <dd>{systemStatus.application}</dd>
                  </div>

                  <div>
                    <dt>Etat</dt>
                    <dd>{systemStatus.status}</dd>
                  </div>
                </dl>
              </div>
          )}

          {error && (
              <div className="error">
                <strong>Connexion impossible</strong>
                <p>{error}</p>
              </div>
          )}

          {!systemStatus && !error && <p>Connexion au serveur...</p>}
        </section>
      </main>
  );
}

export default App;