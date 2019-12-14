using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Office.Interop.Excel;
using _Excel = Microsoft.Office.Interop.Excel;

namespace ExelManipulator
{
    class Excel
    {
        string path = "";
        _Application excel = new _Excel.Application();
        Workbook wb;
        Worksheet ws;

        public Excel()
        {

        }

        public Excel(string path, int Sheet)
        {
            this.path = path;
            wb = excel.Workbooks.Open(path);
            ws = wb.Worksheets[Sheet];
        }

        public void CreateNewFile()
        {
            this.wb = excel.Workbooks.Add(XlWBATemplate.xlWBATWorksheet);
            this.ws = wb.Worksheets[1];
        }

        public void changeWorksheet(int Sheet)
        {
            ws = wb.Worksheets[Sheet];
        }
        public void changeWorksheetName(int Sheet, string name)
        {
            Worksheet temp = wb.Worksheets[Sheet];
            temp.Name = name;
        }
        public void CreateNewSheet()
        {
            Worksheet temptsheet = wb.Worksheets.Add(After: ws);
        }

        public void CreateNewSheet(string content)
        {
            Worksheet temptsheet = wb.Worksheets.Add(After: ws);
            temptsheet.Name = content;
        }

        public string ReadCellS(int row, int line)
        {
            row++;
            line++;
            if (ws.Cells[row, line].Value2 != null)
            {
                try
                {
                    return ws.Cells[row, line].Value2;
                } catch (Exception e)
                {
                    double temp = ws.Cells[row, line].Value2;
                    return temp.ToString();
                }
            }
            else return "";
        }

        public void changeFormat(int row, int line, string format)
        {
            ws.Cells[++row, ++line].NumberFormat = format;
        }

        public double ReadCellD(int row, int line)
        {
            row++;
            line++;
            if (ws.Cells[row, line].Value2 != null) return ws.Cells[row, line].Value2;
            else return 0;
        }

        public void WriteToCell(int row, int line, string content)
        {
            row++;
            line++;
            ws.Cells[row, line].Value2 = content;
        }

        public void Save()
        {
            wb.Save();
        }

        public void SaveAs(string path)
        {
            wb.SaveAs(path);
        }

        public void ChangeText(int row, int line, string font, int size, bool bold, bool italic, bool underline, XlRgbColor color)
        {
            Range range = ws.Range[ws.Cells[++row, ++line],ws.Cells[row,line]];
            range.Font.Name = font;
            range.Font.Size = size;
            range.Font.Bold = bold;
            range.Font.Italic = italic;
            range.Font.Underline = underline;
            range.Interior.Color = color;
        }

        public void close()
        {
            wb.Close();
        }
    }
}
