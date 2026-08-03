import asyncio, http.server, socketserver, threading, os
from playwright.async_api import async_playwright

root = os.path.join(os.getcwd(), "public")
Handler = lambda *a, **k: http.server.SimpleHTTPRequestHandler(*a, directory=root, **k)
httpd = socketserver.TCPServer(("127.0.0.1", 0), Handler)
port = httpd.server_address[1]
threading.Thread(target=httpd.serve_forever, daemon=True).start()

async def main():
    async with async_playwright() as p:
        b = await p.chromium.launch()
        pg = await b.new_page()
        msgs=[]
        pg.on("console", lambda m: msgs.append(m.type+":"+m.text))
        pg.on("pageerror", lambda e: msgs.append("ERR:"+str(e)))
        await pg.goto(f"http://127.0.0.1:{port}/index.html", wait_until="load")
        await pg.wait_for_timeout(900)
        info = await pg.evaluate("""() => {
          return {
            fItem: !!document.getElementById('fItem'),
            fCost: !!document.getElementById('fCost'),
            liveSheets: document.querySelectorAll('.sheet.is-live').length,
            liveForm: document.querySelectorAll('.demo-form').length,
            pagePos: document.getElementById('pagePos')?.textContent,
            stageChildren: document.getElementById('padStage')?.children.length
          };
        }""")
        print("INFO", info)
        await b.close()

asyncio.run(main())
