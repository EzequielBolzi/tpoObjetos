// Crear usuarios
curl -X POST "http://localhost:8080/api/usuarios?contrasena=1234" \
 -H "Content-Type: application/json" \
 -d '{
"nombre": "Luis Martinez",
"correo": "luis@example.com",
"nivelesPorDeporte": { "FUTBOL": "AVANZADO" },
"medioNotificacion": "EMAIL",
"ubicacion": { "latitud": 1, "longitud": 1 }
}'

curl -X POST "http://localhost:8080/api/usuarios?contrasena=1234" \
 -H "Content-Type: application/json" \
 -d '{
"nombre": "Juan Perez",
"correo": "juan@example.com",
"nivelesPorDeporte": { "FUTBOL": "AVANZADO" },
"medioNotificacion": "EMAIL",
"ubicacion": { "latitud": 1, "longitud": 1 }
}'

// Crear partido (con hora actual)
NOW=$(date -u +"%Y-%m-%dT%H:%M:%S.000Z")
curl -X POST "http://localhost:8080/api/partidos?emparejamiento=ubicacion" \
  -H "Content-Type: application/json" \
  -d "{
    \"deporte\": \"FUTBOL\",
    \"cantidadJugadores\": 2,
    \"duracion\": 10,
    \"ubicacion\": { \"latitud\": 1, \"longitud\": 1 },
    \"horario\": \"$NOW\",
\"nivel\": \"AVANZADO\",
\"jugadoresParticipan\": [
{
\"nombre\": \"Luis Martinez\",
\"correo\": \"luis@example.com\",
\"nivelesPorDeporte\": { \"FUTBOL\": \"AVANZADO\" },
\"medioNotificacion\": \"EMAIL\",
\"ubicacion\": { \"latitud\": 1, \"longitud\": 1 }
}
]
}"

// Agregar a jugador al partido
curl -X POST "http://localhost:8080/api/partidos/0/agregar-jugador" \
 -H "Content-Type: application/json" \
 -d '{
"nombre": "Juan Perez",
"correo": "juan@example.com",
"nivelesPorDeporte": { "FUTBOL": "AVANZADO" },
"medioNotificacion": "EMAIL",
"ubicacion": { "latitud": 1, "longitud": 1 }
}'

// Armar partido
curl -X POST "http://localhost:8080/api/partidos/0/armar"

//Confirmar partido
curl -X POST "http://localhost:8080/api/partidos/0/confirmar" \
 -H "Content-Type: application/json" \
 -d '{"nombre": "Luis Martinez", "correo": "luis@example.com"}'

curl -X POST "http://localhost:8080/api/partidos/0/confirmar" \
 -H "Content-Type: application/json" \
 -d '{"nombre": "Juan Perez", "correo": "juan@example.com"}'

// comenzar partido (overrideHorario=true esto lo que hace es que se pueda comenzar el partido sin importar la hora)
curl -X POST "http://localhost:8080/api/partidos/0/comenzar?overrideHorario=true" \
 -H "Content-Type: application/json" \
 -d '{"nombre": "Luis Martinez", "correo": "luis@example.com"}'

// finalizar partido (overrideHorario=true esto lo que hace es que se pueda finalizar el partido sin importar la hora)
curl -X POST "http://localhost:8080/api/partidos/0/finalizar?overrideHorario=true" \
 -H "Content-Type: application/json" \
 -d '{"nombre": "Luis Martinez", "correo": "luis@example.com"}'

// comentar partido
curl -X POST "http://localhost:8080/api/partidos/0/comentar" \
 -H "Content-Type: application/json" \
 -d '{
"usuario": { "nombre": "Juan Perez", "correo": "juan@example.com" },
"comentario": "Me encantó el partido!!"
}'
