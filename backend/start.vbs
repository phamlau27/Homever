Set WshShell = CreateObject("WScript.Shell")
WshShell.CurrentDirectory = "D:\HomEver2\backend"
WshShell.Run "node dist\main.js", 0, False
