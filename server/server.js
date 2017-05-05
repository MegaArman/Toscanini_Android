// Load the http module to create an http server.
const http = require("http");
const fs = require("fs");
const searchFacts = require("./searchFacts");
const createQuery = require("./createQuery");
const port = 8001;

function send404Response(response)
{
	response.writeHead(404,{"Content-Type": "text/plain"});
	response.write("Error 404: Page not found");
	response.end();
}

function onRequest(request, response)
{
	if (request.method === "GET")
	{
		if (request.url === "/")
		{
				response.writeHead(200, {"Content-Type": "text/html"});
				fs.createReadStream("./index.html").pipe(response);
		}
    else if (request.url === "/instructions.html")
		{
				response.writeHead(200, {"Content-Type": "text/html"});
				fs.createReadStream("./instructions.html").pipe(response);
		}
    else if (request.url === "/newfiles")
    {
        response.writeHead(200, {"Content-Type": "text"});
				fs.createReadStream("./newfiles.txt").pipe(response);
    }
    else if (request.url.includes("/?="))
    {
      console.log("query!");
      response.writeHead(200, {"Content-Type": "application/json"});
		  
			const query = createQuery(request.url.split("/?=")[1]);
      if (query === false)
      {
        response.end("Query has a formatting error, please fix");
        return;
      }
      console.log("GET query", query);
      const pdfNames = searchFacts(query).map((xml) => xml.replace("xml", "pdf"));
 			response.end(JSON.stringify(pdfNames));  
    }
    else if (request.url ==="/materialize.css")
    {
    	response.writeHead(200, {"Content-Type": "text/css"});
			fs.createReadStream("./materialize.css").pipe(response);
    }
		else if (request.url === "/main.js")
		{
			response.writeHead(200, {"Content-Type": "text/javascript"});
			fs.createReadStream("./main.js").pipe(response);
		}
    else if (request.url === "/osmd.min.js")
    {
      response.writeHead(200, {"Content-Type": "text/javascript"});
			fs.createReadStream("./osmd.min.js").pipe(response);
    }
    else if (request.url.includes("/pdf_scores/")
             && request.url.includes(".pdf"))
    {
      console.log("pdf requested!");
      const score = request.url.replace("/pdf_scores/", "");
      console.log("score is ", score);
      const readStream = fs.createReadStream("./pdf_scores/" + score);
       
      readStream.on("open", ()=>
      {
        console.log("about to stream");
        response.writeHead(200, {"Content-Type": "text/pdf"});
        readStream.pipe(response);
      });

      readStream.on("error", (err) =>
      {
        send404Response(response);
      }); 
    }
    else
    {
      send404Response(response);
    }

    return;
	}
	else if (request.method === "POST")
	{
		let requestBody = "";
		request.on("data", (data)=> 
		{
			requestBody += data;

			if (requestBody.length > 1e4) //
			{
				response.writeHead(413, "Request Entity Too Large", {"Content-Type": "text/html"});
				response.end("<html>failed</html>");
			}
		});
		request.on("end", ()=> 
		{
			console.log("requestBody", requestBody);
			response.writeHead(200, {"Content-Type": "application/json"});
		  
			const query = (requestBody === "lucky") ? "lucky" : JSON.parse(requestBody);
      const pdfNames = searchFacts(query).map((xml) => xml.replace("xml", "pdf"));
      console.log("Serving...", JSON.stringify(pdfNames));
 			response.end(JSON.stringify(pdfNames)); 
		});
	}
	else
	{
		send404Response(response);
	}
}

const server = http.createServer(onRequest);

// Listen on port, IP defaults to 127.0.0.1
server.listen(port);

// Put a friendly message on the terminal
console.log("Server running at http://127.0.0.1:" + port + "/");
