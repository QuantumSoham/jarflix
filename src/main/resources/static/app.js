const grid = document.getElementById("movie-grid");
const playerView = document.getElementById("player-view");
const video = document.getElementById("video-player");
const backBtn = document.getElementById("back-btn");

fetch("/movies")
    .then(res => res.json())
    .then(movies => {
        movies.forEach(movie => {
            const card = document.createElement("div");
            card.className = "movie-card";

            card.innerHTML = `
                <div class="movie-thumb"></div>
                <div class="movie-title">${movie}</div>
            `;

            card.onclick = () => playMovie(movie);
            grid.appendChild(card);
        });
    });

function playMovie(movie) {
    grid.style.display = "none";
    playerView.classList.remove("hidden");

    const encoded = encodeURIComponent(movie);
    video.src = `/stream/${encoded}`;
    video.play();
}

backBtn.onclick = () => {
    video.pause();
    video.src = "";
    playerView.classList.add("hidden");
    grid.style.display = "grid";
};
