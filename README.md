
# Jarflix 🍿

*A simple video streaming server built from scratch*

Jarflix is a lightweight video streaming platform built using **Spring Boot** and a bit of **Reactive programming**.
At its core, it reads movies from a local folder (for example, an external hard drive) and streams them to any device using standard HTTP video streaming semantics.

This project started as “let me stream movies from my hard drive” and slowly turned into a deep dive into how real video streaming on the web actually works.



## What Jarflix does

* Reads video files from a specified folder
* Lists all available movies
* Streams videos to browsers, phones, TVs, VLC, etc.
* Supports **seeking, pause, resume** using HTTP Range requests
* Works across devices connected to the same network
* Packaged as a **single runnable JAR**

This is **not** a media library manager or a Netflix clone UI-heavy app.
It’s a **correct, standards-compliant video streaming server**.

---

## What Jarflix does NOT do (yet)

* Adaptive bitrate streaming (HLS / DASH)
* Resolution switching
* DRM
* Authentication
* CDN distribution
* Cloud-scale deployment

Those are deliberate omissions. The goal was to first understand the fundamentals properly.

---

## Tech Stack

* Java
* Spring Boot
* Spring WebFlux (Reactive)
* Reactor Netty
* Plain HTML / CSS / JS (for the frontend)
* HTTP Range-based streaming

---

## Why this project exists

Most people think video streaming means “send the whole file to the client”.

That mental model is wrong.

This project was built to understand:

* How browsers actually stream video
* Why `Range` headers exist
* How seeking works under the hood
* Why clients abort connections all the time
* How a server should behave in those cases
* How networking actually works beyond `localhost`

Jarflix is intentionally simple so these concepts stay visible.

---

## How streaming works in Jarflix

* Clients (browsers, VLC, TVs) send `Range` headers like:

  ```
  Range: bytes=5210112-
  ```
* The server responds with:

  ```
  206 Partial Content
  ```

  along with proper `Content-Range`, `Content-Length`, and `Accept-Ranges` headers.
* Seeking the timeline triggers **new HTTP requests** with different byte offsets.
* Clients may abort connections at any time.
  This is normal and expected behavior.

Jarflix handles this correctly by staying stateless and letting the client control chunking.

---

## Running the project

### Prerequisites

* Java 17+
* Maven

### Build

```bash
mvn clean package
```

### Run

```bash
java -jar target/jarflix.jar
```

---

## Configuration

Specify the folder containing your movies:

```yaml
server:
  port: 9090

movies:
  folders:
    - "C:/Movies"
```

You can point this to:

* An external hard drive
* Any local directory
* Multiple folders

---

## Endpoints

### List movies

```
GET /movies
```

Returns a list of available video files.

---

### Stream a movie

```
GET /stream/{movie-name}
```

Supports HTTP Range requests automatically.

Example:

```
http://localhost:9090/stream/sample.mp4
```

---

## Frontend

A simple Netflix-style frontend is served from:

```
http://localhost:9090/
```

* Lists available movies
* Click to play
* Uses the native `<video>` element
* Relies entirely on browser-side chunking

No frameworks, no heavy UI logic.

---

## Networking notes

* `localhost` works only on the same machine
* To stream to other devices, use your LAN IP (e.g. `192.168.x.x`)
* All devices must be on the same network
* Firewall rules may be required to open the port

Public exposure via **Cloudflare Tunnel** or **ngrok** is being explored.

---

## Known limitations

* Single-bitrate files
* No transcoding
* No subtitles handling
* No metadata extraction
* No access control

These are out of scope for the initial learning goal.

---

## What this project taught me

* Video streaming is client-driven
* Chunking is negotiated via HTTP, not magic
* Aborted connections are normal
* Browsers are very aggressive and very smart
* Networking issues look scary until you understand them
* A “simple” streaming server already touches real-world systems concepts

---

## Future ideas (maybe)

* HLS / DASH support
* Adaptive bitrate
* Subtitle support
* Watch progress tracking
* Public hosting
* Better UI for TVs

No promises. This project is about understanding first, scaling later.

---

## Final note

Jarflix is not Netflix.
But it behaves like the **core of a real streaming server**, using the same protocols and assumptions.

If you’re trying to understand how video streaming actually works on the web, this project is a good place to start.

---
