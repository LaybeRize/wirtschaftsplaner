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

        Calculation calc = new Calculation();

        public Form1()
        {
            InitializeComponent();
        }

        private void btn_createFile_Click(object sender, EventArgs e)
        {
            calc.CreateFile(txtB_Dateiname.Text);
        }

        private void btn_openfile_Click(object sender, EventArgs e)
        {
            //calc.OpenFormater(lbl_test);
            calc.OpenFile(txtB_Dateiname.Text);
            MessageBox.Show("Finished");
        }

        private void btn_addChange_Click(object sender, EventArgs e)
        {
            calc.saveNewInfo(txtBox_posX.Text, txtBox_posY.Text, txtBox_Change.Text, txtBox_Start.Text, txtBox_Duration.Text);
            MessageBox.Show("Information has been added");
        }
    }
}
