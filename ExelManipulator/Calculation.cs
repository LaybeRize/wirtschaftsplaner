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
        string[,] format;
        string[,] calcInfo;
        double[,] toLoad;

        public void OpenFormater(System.Windows.Forms.Label lbl_test)
        {
            Excel excel = new Excel(@"format", 3);
            Excel ReadFrom = new Excel();
            try
            {
                Console.WriteLine("ok,cool");
                int month = (int)excel.ReadCellD(0, 0);
                int year = (int)excel.ReadCellD(0, 1);
                bool newFile = false;
                bool make_changes = false;
                //Loads the File to read from
                if (month == 1)
                {
                    int temp = year - 1;
                    ReadFrom = new Excel(temp.ToString(), 12);
                    newFile = true;
                }
                else
                {
                    int temp = month - 1;
                    ReadFrom = new Excel(year.ToString(), temp);
                }
                int change_row = 2;
                while (!excel.ReadCellS(change_row, 0).Equals("x")) change_row++;
                string[,] changes = new string[1,1];
                if (change_row != 2)
                {
                    make_changes = true;
                    changes = new string[change_row - 2, 5];
                    for (int i = 2; i < change_row; i++)
                    {
                        for (int a = 0; a < 5; a++)
                        {
                            changes[i - 2, a] = excel.ReadCellS(i, a);
                            Console.WriteLine(changes[i - 2, a] + "|" + i + "|" + a);
                        }
                        if (excel.ReadCellS(i, 3).Equals("0"))
                        {
                            int dur = int.Parse(excel.ReadCellS(i, 4));
                            dur--;
                            excel.WriteToCell(i, 4, dur.ToString());
                        } else
                        {
                            int start = int.Parse(excel.ReadCellS(i, 3));
                            start--;
                            excel.WriteToCell(i, 3, start.ToString());
                        }
                    }
                }
                excel.Save();
                //load the format
                excel.changeWorksheet(1);
                //checks the row length
                int row = 0;
                while (!excel.ReadCellS(row, 0).Equals("x")) row++;
                int line = 0;
                //checks the line length
                while (!excel.ReadCellS(0, line).Equals("x")) line++;
                lbl_test.Text = row.ToString() + "|" + line.ToString();
                //init all variables
                AllInfos = new string[row, line];
                format = new string[row, line];
                toLoad = new double[row, line];
                calcInfo = new string[row, line];
                //Loads the framework for the reader
                for (int i = 0; i < row; i++)
                {
                    for (int a = 0; a < line; a++)
                    {
                        AllInfos[i, a] = excel.ReadCellS(i, a);
                    }
                }
                //loads the information for the loading
                excel.changeWorksheet(2);
                for (int i = 0; i < row; i++)
                {
                    for (int a = 0; a < line; a++)
                    {
                        calcInfo[i, a] = excel.ReadCellS(i, a);
                    }
                }
                //loads the format
                bool AllFormats = true;
                int counter = 0;
                int lineFormat = 0;
                while (AllFormats)
                {
                    int cur_counter = (int) excel.ReadCellD(row + lineFormat, 0);
                    Console.WriteLine("hey" + cur_counter);
                    for (int i = 0; i < cur_counter; i++)
                    {
                        for (int a = 0; a < line; a++)
                        {
                            Console.WriteLine(counter + "|" + i);
                            format[counter, a] = excel.ReadCellS(row + lineFormat + 1, a);
                        }
                        counter++;
                    }
                    Console.WriteLine("hey" + cur_counter);
                    lineFormat += 2;
                    if (excel.ReadCellD(row + lineFormat, 0) == 0) AllFormats = false;
                }
                //load all the number
                for (int i = 0; i < row; i++)
                {
                    for (int a = 1; a < line; a++)
                    {
                        Console.WriteLine(calcInfo[i, a]);
                        if (!calcInfo[i,a].Equals("") && !calcInfo[i,a].Equals("gen_t"))
                        {
                            Console.WriteLine(i + "|" + a);
                            toLoad[i, a] = ReadFrom.ReadCellD(i, a);
                            Console.WriteLine(toLoad[i, a]);
                        }
                    }
                }
                Console.WriteLine(newFile);
                if (newFile)
                {
                    ReadFrom = new Excel();
                    ReadFrom.changeWorksheetName(1, allMonths[0]);
                    ReadFrom.SaveAs(year.ToString());
                } else
                {
                    Console.WriteLine("asdaf");
                    ReadFrom.CreateNewSheet(allMonths[month-1]);
                    ReadFrom.changeWorksheet(month);
                }
                if (make_changes)
                {
                    for (int i = 0; i < change_row - 2; i++)
                    {
                        Console.WriteLine(i + "|" + change_row);
                        if (changes[i, 3].Equals("0"))
                        {
                            int pos_x = int.Parse(changes[i, 0]);
                            int pos_y = int.Parse(changes[i, 1]);
                            if (calcInfo[pos_x, pos_y].Equals("load") || calcInfo[pos_x, pos_y].Equals("work"))
                            {
                                int start_y = pos_y;
                                while (!calcInfo[pos_x, start_y].Equals("work")) start_y--;
                                double[] allChanges = changeRate(pos_y - start_y, double.Parse(changes[i, 2]),toLoad[pos_x,start_y], 
                                    toLoad[pos_x, start_y + 1], toLoad[pos_x, start_y + 2], toLoad[pos_x, start_y + 3], toLoad[pos_x, start_y + 4], toLoad[pos_x, start_y + 5]);
                                toLoad[pos_x, start_y] = allChanges[0];
                                toLoad[pos_x, start_y + 1] = allChanges[1];
                                toLoad[pos_x, start_y + 2] = allChanges[2];
                                toLoad[pos_x, start_y + 3] = allChanges[3];
                                toLoad[pos_x, start_y + 4] = allChanges[4];
                                toLoad[pos_x, start_y + 5] = allChanges[5];
                            }
                            else
                            {
                                Console.WriteLine(calcInfo[pos_x, pos_y]);
                                double newWorth = double.Parse(calcInfo[pos_x, pos_y]);
                                newWorth += double.Parse(changes[i, 2]);
                                calcInfo[pos_x, pos_y] = newWorth.ToString();
                            }
                        }
                    }
                }
                double infaltion = 0;
                excel.changeWorksheet(3);
                for (int i = 0; i < row; i++)
                {
                    for (int a = 0; a < line; a++)
                    {
                        ReadFrom.changeFormat(i, a, format[i, a].Substring(1));
                        double temp;
                        ReadFrom.changeColor(i, a, returnColor(calcInfo[i, 0]));
                        if (!AllInfos[i, a].Equals(""))
                        {
                            Console.WriteLine(AllInfos[i, a]);
                            ReadFrom.WriteToCell(i, a, AllInfos[i, a]);
                        } else if (calcInfo[i, a].StartsWith("!"))
                        {
                            if (calcInfo[i, a].StartsWith("!SUMME")) ReadFrom.WriteToCell(i, a, convertSumme(calcInfo[i, a]));
                            else ReadFrom.WriteToCell(i, a, "=" + calcInfo[i, a].Substring(1));
                        } else if (double.TryParse(calcInfo[i,a],out temp))
                        {
                            temp = manipulateChange(temp);
                            double calculated = toLoad[i, a - 1] * (1 + temp);
                            ReadFrom.WriteToCell(i, a, temp.ToString());
                            ReadFrom.WriteToCell(i, a-1, calculated.ToString());
                            if (AllInfos[i, a - 2].Equals("Infaltion")) infaltion = calculated;
                        } else if (calcInfo[i,a].Equals("work"))
                        {
                            ReadFrom.WriteToCell(i,a, toLoad[i,a].ToString());
                            ReadFrom.WriteToCell(i, a+1, toLoad[i, a+1].ToString());
                            ReadFrom.WriteToCell(i, a+2, toLoad[i, a+2].ToString());
                            ReadFrom.WriteToCell(i, a+3, toLoad[i, a+3].ToString());
                            ReadFrom.WriteToCell(i, a+4, toLoad[i, a+4].ToString());
                            ReadFrom.WriteToCell(i, a + 5, toLoad[i, a + 5].ToString());
                        }
                    }
                    //Console.WriteLine("lol");
                }
                ReadFrom.Save();
                Console.WriteLine(ReadFrom.ReadCellS(3, 1));
                //Console.WriteLine("sdlliiki");
                for (int i = 0; i < row; i++)
                {
                    for (int a = 0; a < line; a++)
                    {
                        double temp;
                        if (calcInfo[i, a].Equals("inf"))
                        {
                            if (!calcInfo[i, a - 2].Equals("calc"))
                            {
                                temp = ReadFrom.ReadCellD(i, a - 3);
                                temp = temp * (1 + infaltion);
                                ReadFrom.WriteToCell(i, a - 1, temp.ToString());
                            }
                        }
                        else if (calcInfo[i, a].Equals("calc"))
                        {
                            double read = ReadFrom.ReadCellD(i, a - 1);
                            ReadFrom.WriteToCell(i, a, writeEquation(read,toLoad[i,a-1]).ToString());
                        }
                    }
                }
                ReadFrom.Save();
                Console.WriteLine(ReadFrom.ReadCellS(3, 1));
                for (int i = 0; i < row; i++)
                {
                    for (int a = 0; a < line; a++)
                    {
                        if (calcInfo[i, a].Equals("inf"))
                        {
                            double read = ReadFrom.ReadCellD(i, a - 1);
                            ReadFrom.WriteToCell(i, a, writeEquation(read, toLoad[i, a - 1]).ToString());
                        }
                    }
                }
                ReadFrom.Save();
                Console.WriteLine(ReadFrom.ReadCellS(3, 1));
                excel.changeWorksheet(3);
                for (int i = 2; i < change_row; i++)
                {
                    if (excel.ReadCellS(i,3).Equals("0") && excel.ReadCellS(i, 4).Equals("0"))
                    {
                        for (int a = i; a < change_row + 1; a++)
                        {
                            for (int t = 0; t < 5; t++)
                            {
                                excel.WriteToCell(a, t, excel.ReadCellS(a + 1, t));
                            }
                        }
                        change_row--;
                        i--;
                    }
                }
                int mon = int.Parse(excel.ReadCellS(0, 0));
                mon++;
                if (mon > 12)
                {
                    int y = int.Parse(excel.ReadCellS(0, 1));
                    y++;
                    excel.WriteToCell(0, 1, y.ToString());
                    mon = 1;
                }
                excel.WriteToCell(0, 0, mon.ToString());
                excel.Save();
            }
            catch (Exception e)
            {
                Console.Write(e.StackTrace);
            }
            finally
            {
                ReadFrom.close();
                excel.close();
            }
        }

        public void saveNewInfo(string pos_x, string pos_y, string change, string start, string duration)
        {
            Excel excel = new Excel(@"format", 3);
            int change_row = 2;
            while (!excel.ReadCellS(change_row, 0).Equals("x")) change_row++;
            excel.WriteToCell(change_row, 0, pos_x);
            excel.WriteToCell(change_row, 1, pos_y);
            excel.WriteToCell(change_row, 2, change);
            excel.WriteToCell(change_row, 3, start);
            excel.WriteToCell(change_row, 4, duration);
            excel.WriteToCell(change_row + 1, 0, "x");
            excel.Save();
            excel.close();
        }

        public double[] changeRate(int pos, double change, double _1, double _2 , double _3, double _4, double _5, double _6)
        {
            //TODO: Needs a rework
            double[] allW = new double[6];
            switch(pos)
            {
                case 0:
                    _1 += change;
                    _2 -= change * 0.7;
                    _3 -= change * 0.2;
                    _4 -= change * 0.07;
                    _5 -= change * 0.03;
                    break;
                case 1:
                    _1 -= change * 0.35;
                    _2 += change;
                    _3 -= change * 0.35;
                    _4 -= change * 0.2;
                    _5 -= change * 0.07;
                    _6 -= change * 0.03;
                    break;
                case 2:
                    _1 -= change * 0.125;
                    _2 -= change * 0.35;
                    _3 += change;
                    _4 -= change * 0.35;
                    _5 -= change * 0.125;
                    _6 -= change * 0.05;
                    break;
                case 3:
                    _6 -= change * 0.125;
                    _5 -= change * 0.35;
                    _4 += change;
                    _3 -= change * 0.35;
                    _2 -= change * 0.125;
                    _1 -= change * 0.05;
                    break;
                case 4:
                    _6 -= change * 0.35;
                    _5 += change;
                    _4 -= change * 0.35;
                    _3 -= change * 0.2;
                    _2 -= change * 0.07;
                    _1 -= change * 0.03;
                    break;
                case 5:
                    _6 += change;
                    _5 -= change * 0.7;
                    _4 -= change * 0.2;
                    _3 -= change * 0.07;
                    _2 -= change * 0.03;
                    break;
            }
            allW[0] = _1;
            allW[1] = _2;
            allW[2] = _3;
            allW[3] = _4;
            allW[4] = _5;
            allW[5] = _6;
            return allW;
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
                case "grey":
                    return XlRgbColor.rgbGray;
                default:
                    return XlRgbColor.rgbWhite;
            }
        }

        public string convertSumme(string input)
        {
            string number = input.Substring(7);
            number = number.Substring(0, number.Length - 1);
            string[] numbers = number.Split(':');
            int[] counting = new int[2];
            string letter = numbers[0].Substring(0, 1);
            counting[0] = int.Parse(numbers[0].Substring(1));
            counting[1] = int.Parse(numbers[1].Substring(1));
            input = "=";
            for (int i = counting[0]; i - 1 < counting[1]; i++)
            {
                input = input + letter + i.ToString() + "+";
            }
            return input.Substring(0,input.Length-1);
        }

        public double writeEquation(double read, double info)
        {
            read = -1 * (1-(read/info));
            return read;
        }

        public double manipulateChange(double info)
        {
            Random rand = new Random();
            double temp = rand.NextDouble();
            temp *= 0.01;
            temp -= 0.005;
            info += temp;
            return info;
        }

        public string Trend(double info)
        {
            if (info > 0.005)
            {
                return "+";
            } else if (info < -0.005)
            {
                return "-";
            } else
            {
                return "/";
            }
        }


        public void OpenFile(string filename)
        {
            Excel excel = new Excel(filename, 3);
            try
            {
                int change_row = 2;
                while (!excel.ReadCellS(change_row, 0).Equals("x")) change_row++;
                for (int i = 2; i < change_row; i++)
                {
                    if (excel.ReadCellS(i, 3).Equals("0") && excel.ReadCellS(i, 4).Equals("0"))
                    {
                        for (int a = i; a < change_row + 1; a++)
                        {
                            for (int t = 0; t < 5; t++)
                            {
                                excel.WriteToCell(a, t, excel.ReadCellS(a + 1, t));
                            }
                        }
                        change_row--;
                        i--;
                    }
                }
                excel.Save();
            }
            catch (Exception e)
            {
                Console.Write(e.StackTrace);
            }
            excel.close();
        }
    }
}
