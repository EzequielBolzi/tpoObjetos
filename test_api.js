const API_URL = "http://localhost:8080/api";

async function crearUsuario(usuario) {
  const res = await fetch(`${API_URL}/usuarios`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(usuario),
  });
  return res.json().catch(() => ({}));
}

async function crearPartido(partido, emparejamiento) {
  const res = await fetch(
    `${API_URL}/partidos?emparejamiento=${emparejamiento}`,
    {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(partido),
    }
  );
  return res;
}

async function listarPartidos() {
  const res = await fetch(`${API_URL}/partidos`);
  return res.json();
}

async function buscarPorNivel(nivel) {
  const res = await fetch(`${API_URL}/partidos?nivel=${nivel}`);
  return res.json();
}

async function buscarPorUbicacion(lat, lng, cantidad) {
  const res = await fetch(
    `${API_URL}/partidos?lat=${lat}&lng=${lng}&cantidad=${cantidad}`
  );
  return res.json();
}

async function agregarJugador(id, usuario) {
  const res = await fetch(`${API_URL}/partidos/${id}/agregar-jugador`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(usuario),
  });
  return res;
}

async function armarPartido(id) {
  return fetch(`${API_URL}/partidos/${id}/armar`, { method: "POST" });
}

async function confirmarPartido(id, usuario) {
  return fetch(`${API_URL}/partidos/${id}/confirmar`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(usuario),
  });
}

async function comenzarPartido(id, usuario) {
  return fetch(`${API_URL}/partidos/${id}/comenzar`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(usuario),
  });
}

async function finalizarPartido(id, usuario) {
  return fetch(`${API_URL}/partidos/${id}/finalizar`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(usuario),
  });
}

async function comentarPartido(id, usuario, comentario) {
  return fetch(`${API_URL}/partidos/${id}/comentar`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ usuario, comentario }),
  });
}

(async () => {
  console.log("Creando usuarios...");
  const luis = {
    nombre: "Luis Martinez",
    correo: "luis@example.com",
    nivelesPorDeporte: { FUTBOL: "AVANZADO" },
    medioNotificacion: "EMAIL",
    ubicacion: { latitud: -34.6, longitud: -58.4 },
  };
  const ana = {
    nombre: "Ana Gomez",
    correo: "ana@example.com",
    nivelesPorDeporte: { BASQUET: "INTERMEDIO" },
    medioNotificacion: "SMS",
    ubicacion: { latitud: -34.61, longitud: -58.42 },
  };
  const juan = {
    nombre: "Juan Perez",
    correo: "juan@example.com",
    nivelesPorDeporte: { FUTBOL: "PRINCIPIANTE" },
    medioNotificacion: "EMAIL",
    ubicacion: { latitud: -34.62, longitud: -58.41 },
  };
  await crearUsuario(luis);
  await crearUsuario(ana);
  await crearUsuario(juan);

  console.log("Creando partido...");
  const partido = {
    deporte: "FUTBOL",
    cantidadJugadores: 3,
    duracion: 90,
    ubicacion: { latitud: -34.6, longitud: -58.4 },
    horario: "2024-06-10T15:00:00.000+00:00",
    nivel: "AVANZADO",
    jugadoresParticipan: [luis],
  };
  await crearPartido(partido, "nivel");

  console.log("Listando partidos...");
  const partidos = await listarPartidos();
  console.log("Partidos:", partidos);
  const partidoId = partidos[0]?.id;
  if (!partidoId) {
    console.error("No se pudo obtener el ID del partido.");
    return;
  }
  console.log("ID del partido creado:", partidoId);

  console.log("Buscando partidos por nivel AVANZADO...");
  const partidosPorNivel = await buscarPorNivel("AVANZADO");
  console.log("Partidos por nivel:", partidosPorNivel);

  console.log("Buscando partidos cercanos a Ana...");
  const partidosPorUbicacion = await buscarPorUbicacion(-34.61, -58.42, 2);
  console.log("Partidos por ubicación:", partidosPorUbicacion);

  console.log("Agregando Ana al partido...");
  await agregarJugador(partidoId, ana);

  console.log("Armando partido...");
  await armarPartido(partidoId);

  console.log("Confirmando partido (Luis)...");
  await confirmarPartido(partidoId, luis);
  console.log("Confirmando partido (Juan)...");
  await confirmarPartido(partidoId, juan);

  console.log("Comenzando partido...");
  await comenzarPartido(partidoId, luis);

  console.log("Finalizando partido...");
  await finalizarPartido(partidoId, luis);

  console.log("Comentando partido...");
  await comentarPartido(partidoId, juan, "Me encanto el partido!!");

  console.log("Pruebas finalizadas.");
})();
