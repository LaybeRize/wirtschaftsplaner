using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using Microsoft.Office.Interop.Excel;

namespace ExelManipulator
{
    public partial class Form1 : Form
    {
        string[] allMonths = {"Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"};

        public Form1()
        {
            InitializeComponent();
        }

        public void CreateFile(string filename)
        {
            Excel excel = new Excel();
            excel.CreateNewFile();
            excel.changeWorksheetName(1, "temp");
            excel.SaveAs(filename);
            excel.close();
        }
        public void OpenFormater()
        {
            Excel excel = new Excel(@"format", 1);
            try
            {
                int row = 0;
                while (!excel.ReadCellS(row, 0).Equals("x")) row++;
                row--;
                int line = 0;
                while (!excel.ReadCellS(0, line).Equals("x")) line++;
                line--;
                lbl_test.Text = row.ToString() + "|" + line.ToString();
                string[] AllInfos = new string[line + 1];
                string[] rowInfo = new string[line + 1];
                string[] format = new string[line + 1];
                int extra = 0;
                for (int i = 0; i < row + 1; i++)
                {
                    for (int a = 0; a < line + 1; a++)
                    {
                        AllInfos[a] = excel.ReadCellS(i, a);
                    }
                    excel.changeWorksheet(2);
                    string color = excel.ReadCellS(i, 0);
                    if (color.Equals("gray")) extra++;
                    for (int a = 0; a < line + 1; a++)
                    {
                        format[a] = excel.ReadCellS(line + 1 + extra, a).Substring(1);
                    }
                    excel.changeWorksheet(3);
                    string month = allMonths[(int)excel.ReadCellD(0, 0) - 1];
                    int monthAdd = (int)excel.ReadCellD(0, 0) + 1;
                    int year = (int)excel.ReadCellD(0, 1);
                    Excel writeTo;
                    if (monthAdd == 2)
                    {
                        int oldyear = year - 1;
                        Excel readFrom = new Excel(oldyear.ToString(), 12);
                        writeTo = new Excel();
                        writeTo.CreateNewFile();
                        writeTo.changeWorksheetName(1,month);


                    }
                    if (monthAdd > 12) excel.WriteToCell(0, 0, "1");
                    else excel.WriteToCell(0, 0, monthAdd.ToString());                    
                }
            } catch (Exception e)
            {
                lbl_test.Text = e.StackTrace;
            } finally
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
            }
            return XlRgbColor.rgbWhite;
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
            MessageBox.Show(outputString);
            excel.close();
        }

        private void btn_createFile_Click(object sender, EventArgs e)
        {
            CreateFile(txtB_Dateiname.Text);
        }

        private void btn_openfile_Click(object sender, EventArgs e)
        {
            //OpenFile(txtB_Dateiname.Text);
            OpenFormater();
        }
    }
}
