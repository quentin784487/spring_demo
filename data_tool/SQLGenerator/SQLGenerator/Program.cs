// See https://aka.ms/new-console-template for more information

using System.Text.RegularExpressions;

Console.WriteLine("Hello, World!");

string folderPath = @"C:\temp\data";

string[] files = Directory.GetFiles(folderPath);

string sql = "INSERT INTO publishers (name) VALUES ";

foreach (string file in Directory.EnumerateFiles(folderPath))
{
    foreach (string line in File.ReadLines(file))
    {
        //string test = "Quentin's";
        if (line == "Starlight Software")
        {
            
        }

        string escapedLine = Regex.Replace(line, "'", "''");
        escapedLine = Regex.Replace(escapedLine, "’", "''");
        sql += "('" + escapedLine + "'), ";
    }
}    

Console.Write(sql);

