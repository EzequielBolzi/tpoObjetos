const API_URL = "http://localhost:8080/api";

async function post(url, data) {
  const res = await fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  let text = await res.text();
  let json;
  try {
    json = JSON.parse(text);
  } catch {
    json = text;
  }
  return { status: res.status, body: json };
}

async function createUser(user, password) {
  // Create user with password
  await fetch(
    `${API_URL}/usuarios?contrasena=${encodeURIComponent(password)}`,
    {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(user),
    }
  );
  // Fetch user by name to get the ID
  return await fetch(
    `${API_URL}/usuarios/${encodeURIComponent(user.nombre)}`
  ).then((r) => r.json());
}

async function main() {
  // 1. Crear usuarios (con password) y obtenerlos con ID
  const luisData = {
    nombre: "Luis Martinez",
    correo: "joseluiskoller98@gmail.com",
    nivelesPorDeporte: { FUTBOL: "AVANZADO" },
    medioNotificacion: "EMAIL",
    ubicacion: { latitud: -34.6, longitud: -58.4 },
  };
  const anaData = {
    nombre: "Ana Gomez",
    correo: "hi@0xkoller.me",
    nivelesPorDeporte: { BASQUET: "INTERMEDIO" },
    medioNotificacion: "EMAIL",
    ubicacion: { latitud: -34.61, longitud: -58.42 },
  };
  const luis = await createUser(luisData, "1234");
  const ana = await createUser(anaData, "1234");

  // 2. Crear partido (Luis como creador, usando el objeto con ID)
  const partido = {
    deporte: "FUTBOL",
    cantidadJugadores: 2,
    duracion: 90,
    ubicacion: { latitud: -34.6, longitud: -58.4 },
    horario: new Date(Date.now() + 60000).toISOString(), // 1 min en el futuro
    nivel: "AVANZADO",
    jugadoresParticipan: [luis],
  };
  await post(`${API_URL}/partidos?emparejamiento=nivel`, partido);

  // 3. Obtener el ID del partido
  const partidos = await fetch(`${API_URL}/partidos`).then((r) => r.json());
  const partidoId = partidos[0]?.id;
  if (!partidoId) {
    console.error("No se pudo obtener el ID del partido.");
    return;
  }
  console.log("ID del partido creado:", partidoId);

  // 4. Agregar Ana al partido (usando el objeto con ID)
  await post(`${API_URL}/partidos/${partidoId}/agregar-jugador`, ana);

  // 5. Armar partido
  let res = await fetch(`${API_URL}/partidos/${partidoId}/armar`, {
    method: "POST",
  });
  console.log("Armar partido:", res.status);

  // 6. Confirmar partido (Luis y Ana)
  res = await post(`${API_URL}/partidos/${partidoId}/confirmar`, luis);
  console.log("Confirmar partido (Luis):", res.status);
  res = await post(`${API_URL}/partidos/${partidoId}/confirmar`, ana);
  console.log("Confirmar partido (Ana):", res.status);

  // 7. Comenzar partido (Luis)
  res = await post(`${API_URL}/partidos/${partidoId}/comenzar`, luis);
  console.log("Comenzar partido:", res.status);

  // 8. Finalizar partido (Luis)
  res = await post(`${API_URL}/partidos/${partidoId}/finalizar`, luis);
  console.log("Finalizar partido:", res.status);

  // 9. Comentar partido (Luis)
  res = await post(`${API_URL}/partidos/${partidoId}/comentar`, {
    usuario: luis,
    comentario: "¡Gran partido!",
  });
  console.log("Comentar partido:", res.status);

  console.log(
    "Prueba finalizada. Revisa los logs del backend para ver las notificaciones y mails."
  );
}

main();
