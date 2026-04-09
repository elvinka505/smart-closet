document.addEventListener("DOMContentLoaded", function () {
    if (document.getElementById("wishlist-body")) {
        loadWishlist();
    }
});

function getCsrf() {
    return {
        token: document.getElementById("csrf-token").value,
        header: document.getElementById("csrf-header").value
    };
}

function loadWishlist() {
    fetch("/api/wishlist")
        .then(res => res.json())
        .then(data => {
            const tbody = document.getElementById("wishlist-body");
            tbody.innerHTML = "";
            data.forEach(item => {
                const row = document.createElement("tr");
                row.innerHTML =
                    "<td>" + (item.name || "") + "</td>" +
                    "<td>" + (item.price || "") + "</td>" +
                    "<td>" + (item.url ? "<a href='" + item.url + "' target='_blank'>Ссылка</a>" : "") + "</td>" +
                    "<td>" + (item.note || "") + "</td>" +
                    "<td><button class='btn btn-danger' onclick='deleteWish(" + item.id + ")'>Удалить</button></td>";
                tbody.appendChild(row);
            });
        });
}

function addWish() {
    const csrf = getCsrf();
    const wish = {
        name: document.getElementById("wish-name").value,
        price: document.getElementById("wish-price").value || null,
        url: document.getElementById("wish-url").value,
        note: document.getElementById("wish-note").value
    };

    fetch("/api/wishlist", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            [csrf.header]: csrf.token
        },
        body: JSON.stringify(wish)
    })
        .then(res => res.json())
        .then(() => {
            document.getElementById("wish-name").value = "";
            document.getElementById("wish-price").value = "";
            document.getElementById("wish-url").value = "";
            document.getElementById("wish-note").value = "";
            loadWishlist();
        });
}

function deleteWish(id) {
    const csrf = getCsrf();
    fetch("/api/wishlist/" + id, {
        method: "DELETE",
        headers: {
            [csrf.header]: csrf.token
        }
    }).then(() => loadWishlist());
}