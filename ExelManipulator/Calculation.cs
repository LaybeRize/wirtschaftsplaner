using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Office.Interop.Excel;


namespace ExelManipulator
{
    class Calculation
    {

        string[] allMonths = { "Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember" };

        public void CreateFile(string filename)
        {
            Excel excel = new Excel();
            excel.CreateNewFile();
            excel.changeWorksheetName(1, "temp");
            excel.SaveAs(filename);
            excel.close();
        }


        string[,] AllInfos;
        string[] color;
        string[,] format;
        double[,] loadedNumbers;


        public void OpenFormater(System.Windows.Forms.Label lbl_test)
        {
            Excel excel = new Excel(@"format", 3);
            try
            {
                int month = (int)excel.ReadCellD(0, 0);
                int year = (int)excel.ReadCellD(0, 1);
                Excel ReadFrom;
                if (month == 1)
                {
                    int temp = year - 1;
                    ReadFrom = new Excel(temp.ToString(), 12);
                }
                else
                {
                    int temp = month - 1;
                    ReadFrom = new Excel(year.ToString(), temp);
                }
                excel.changeWorksheet(1);
                int row = 0;
                while (!excel.ReadCellS(row, 0).Equals("x")) row++;
                int line = 0;
                while (!excel.ReadCellS(0, line).Equals("x")) line++;
                lbl_test.Text = row.ToString() + "|" + line.ToString();
                AllInfos = new string[row, line];
                format = new string[row, line];
                loadedNumbers = new double[row, line];
                for (int i = 0; i < row; i++)
                {
                    for (int a = 0; a < line; a++)
                    {
                        AllInfos[i, a] = excel.ReadCellS(i, a);
                        if (AllInfos[i, a].Equals("load")) loadedNumbers[i, a] = ReadFrom.ReadCellD(i, a);
                    }
                }
                excel.changeWorksheet(2);
                for (int i = 0; i < row; i++)
                {
                    color[i] = excel.ReadCellS(i, 0);
                }
                bool AllFormats = true;
                int counter = 0;
                int lineFormat = 0;
                while (AllFormats)
                {
                    int cur_counter = (int) excel.ReadCellD(row + lineFormat, 0);
                    for (int i = 0; i < cur_counter; i++)
                    {
                        for (int a = 0; a < line; a++)
                        {
                            format[counter++, a] = excel.ReadCellS(row + lineFormat + 1, a);
                        }
                    }
                    lineFormat += 2;
                    if (excel.ReadCellD(row + lineFormat, 0) == 0) AllFormats = false;
                }

            }
            catch (Exception e)
            {
                lbl_test.Text = e.StackTrace;
            }
            finally
            {
                excel.close();
            }
        }

        public XlRgbColor returnColor(string name)
        {
            switch (name)
            {
                case "red":
                    return XlRgbColor.rgbOrangeRed;
                case "dark_red":
                    return XlRgbColor.rgbRed;
                case "yellow":
                    return XlRgbColor.rgbLightYellow;
                case "green":
                    return XlRgbColor.rgbGreen;
                case "light_gray":
                    return XlRgbColor.rgbLightGray;
                case "blue":
                    return XlRgbColor.rgbLightBlue;
                case "dark_blue":
                    return XlRgbColor.rgbBlue;
                case "gray":
                    return XlRgbColor.rgbGray;
                default:
                    return XlRgbColor.rgbWhite;
            }
        }

        public void OpenFile(string filename)
        {
            Excel excel = new Excel(filename, 1);
            excel.WriteToCell(0, 0, "1293");
            excel.WriteToCell(0, 1, "=A1*2");
            excel.Save();
            string outputString = "";
            try
            {
                excel.changeFormat(0, 2, "0.00");
                excel.WriteToCell(0, 2, "12.223");
                excel.ChangeText(0, 2, "Calibri", 11, true, false, false, XlRgbColor.rgbAzure);
                excel.Save();
                double temp = excel.ReadCellD(0, 1);
                outputString = temp.ToString();
            }
            catch (Exception e)
            {
                Console.Write(e.StackTrace);
            }
            excel.close();
        }
    }
}
