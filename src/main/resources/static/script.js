async function fetchData() {
  try {
    let response = await fetch("/api/mergedData");
    let data = await response.json();
    console.log("Received Data: ", data);

    let tableBody = document.querySelector("#dataTable");
    tableBody.innerHTML = "";

    if (data.length === 0) {
      tableBody.innerHTML = "<tr><td colspan='6'>No data found</td></tr>";
      return;
    }

    data.forEach((item) => {
      let row = `<tr>
                        <td>${item.id}</td>
                        <td>${item.latitude}</td>
                        <td>${item.longitude}</td>
                        <td>${item.type}</td>
                        <td>${item.rating}</td>
                        <td>${item.reviews}</td>
                    </tr>`;
      tableBody.innerHTML += row;
    });
  } catch (error) {
    console.error("Error fetching data:", error);
  }
}

async function fetchValidCounts() {
  try {
    let response = await fetch("/api/countValidPoints");
    let data = await response.json();
    console.log("Valid Counts:", data);

    let formattedData = Object.entries(data).map(([type, count]) => ({
      type,
      count,
    }));

    displayResults(formattedData, ["Type", "Count"], {
      Type: "type",
      Count: "count",

    });
  } catch (error) {
    console.error("Error fetching valid counts:", error);
  }
}

async function fetchAverageRatings() {
  try {
    let response = await fetch("/api/averageRating");
    let data = await response.json();
    console.log("Average Ratings:", data);

    let formattedData = Object.entries(data).map(([type, rating]) => ({
      type,
      rating,
    }));

    displayResults(formattedData, ["Type", "Average Rating"], {
      Type: "type",
      "Average Rating": "rating",
    });
  } catch (error) {
    console.error("Error fetching average ratings:", error);
  }
}

async function fetchMostReviewed() {
  try {
    let response = await fetch("/api/mostReviews");
    let data = await response.json();
    console.log("Most Reviewed Location:", data);

    if (data.error) {
      document.querySelector(
        "#resultTable"
      ).innerHTML = `<tr><td colspan='3'>${data.error}</td></tr>`;
    } else {
      displayResults([data], ["ID", "Type", "Reviews"], {
        ID: "id",
        Type: "type",
        Reviews: "reviews",
      });
    }
  } catch (error) {
    console.error("Error fetching most reviewed location:", error);
  }
}

async function fetchIncompleteData() {
  try {
    let response = await fetch("/api/incompleteData");
    let data = await response.json();
    console.log("Incomplete Data:", data);

    if (data.length === 0) {
      document.querySelector(
        "#resultBody"
      ).innerHTML = `<tr><td colspan='6'>All locations have complete data!</td></tr>`;
      return;
    }

    displayResults(
      data,
      ["ID", "Latitude", "Longitude", "Type", "Rating", "Reviews"],
      {
        ID: "ID",
        Latitude: "Latitude",
        Longitude: "Longitude",
        Type: "Type",
        Rating: "Rating",
        Reviews: "Reviews",
      }
    );
  } catch (error) {
    console.error("Error fetching incomplete data:", error);
  }
}

function displayResults(data, headers) {
  let resultHeader = document.querySelector("#resultHeader");
  let resultBody = document.querySelector("#resultBody");

  resultHeader.innerHTML = "";
  resultBody.innerHTML = "";

  headers.forEach((header) => {
    resultHeader.innerHTML += `<th>${header}</th>`;
  });

  if (Array.isArray(data)) {
    data.forEach((item) => {
      let row = `<tr>`;
      headers.forEach((header) => {
        let key = header.toLowerCase().replace(" ", "");
        row += `<td>${
          item[key] || item[Object.keys(item)[headers.indexOf(header)]]
        }</td>`;
      });
      row += `</tr>`;
      resultBody.innerHTML += row;
    });
  }
}
