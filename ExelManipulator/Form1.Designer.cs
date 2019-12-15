namespace ExelManipulator
{
    partial class Form1
    {
        /// <summary>
        /// Erforderliche Designervariable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Verwendete Ressourcen bereinigen.
        /// </summary>
        /// <param name="disposing">True, wenn verwaltete Ressourcen gelöscht werden sollen; andernfalls False.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Vom Windows Form-Designer generierter Code

        /// <summary>
        /// Erforderliche Methode für die Designerunterstützung.
        /// Der Inhalt der Methode darf nicht mit dem Code-Editor geändert werden.
        /// </summary>
        private void InitializeComponent()
        {
            this.txtB_Dateiname = new System.Windows.Forms.TextBox();
            this.lbl_dateiname = new System.Windows.Forms.Label();
            this.btn_createFile = new System.Windows.Forms.Button();
            this.btn_openfile = new System.Windows.Forms.Button();
            this.lbl_test = new System.Windows.Forms.Label();
            this.txtBox_posX = new System.Windows.Forms.TextBox();
            this.txtBox_posY = new System.Windows.Forms.TextBox();
            this.txtBox_Change = new System.Windows.Forms.TextBox();
            this.txtBox_Start = new System.Windows.Forms.TextBox();
            this.txtBox_Duration = new System.Windows.Forms.TextBox();
            this.lbl_duration = new System.Windows.Forms.Label();
            this.lbl_change = new System.Windows.Forms.Label();
            this.lbl_posX = new System.Windows.Forms.Label();
            this.lbl_posY = new System.Windows.Forms.Label();
            this.lbl_start = new System.Windows.Forms.Label();
            this.btn_addChange = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // txtB_Dateiname
            // 
            this.txtB_Dateiname.Location = new System.Drawing.Point(21, 81);
            this.txtB_Dateiname.Name = "txtB_Dateiname";
            this.txtB_Dateiname.Size = new System.Drawing.Size(100, 20);
            this.txtB_Dateiname.TabIndex = 1;
            // 
            // lbl_dateiname
            // 
            this.lbl_dateiname.AutoSize = true;
            this.lbl_dateiname.Location = new System.Drawing.Point(18, 65);
            this.lbl_dateiname.Name = "lbl_dateiname";
            this.lbl_dateiname.Size = new System.Drawing.Size(119, 13);
            this.lbl_dateiname.TabIndex = 2;
            this.lbl_dateiname.Text = "Name der Formattabelle";
            // 
            // btn_createFile
            // 
            this.btn_createFile.Location = new System.Drawing.Point(21, 39);
            this.btn_createFile.Name = "btn_createFile";
            this.btn_createFile.Size = new System.Drawing.Size(75, 23);
            this.btn_createFile.TabIndex = 3;
            this.btn_createFile.Text = "Create File";
            this.btn_createFile.UseVisualStyleBackColor = true;
            this.btn_createFile.Click += new System.EventHandler(this.btn_createFile_Click);
            // 
            // btn_openfile
            // 
            this.btn_openfile.Location = new System.Drawing.Point(120, 38);
            this.btn_openfile.Name = "btn_openfile";
            this.btn_openfile.Size = new System.Drawing.Size(75, 23);
            this.btn_openfile.TabIndex = 4;
            this.btn_openfile.Text = "Open File";
            this.btn_openfile.UseVisualStyleBackColor = true;
            this.btn_openfile.Click += new System.EventHandler(this.btn_openfile_Click);
            // 
            // lbl_test
            // 
            this.lbl_test.AutoSize = true;
            this.lbl_test.Location = new System.Drawing.Point(18, 118);
            this.lbl_test.Name = "lbl_test";
            this.lbl_test.Size = new System.Drawing.Size(0, 13);
            this.lbl_test.TabIndex = 5;
            // 
            // txtBox_posX
            // 
            this.txtBox_posX.Location = new System.Drawing.Point(21, 187);
            this.txtBox_posX.Name = "txtBox_posX";
            this.txtBox_posX.Size = new System.Drawing.Size(100, 20);
            this.txtBox_posX.TabIndex = 6;
            // 
            // txtBox_posY
            // 
            this.txtBox_posY.Location = new System.Drawing.Point(148, 187);
            this.txtBox_posY.Name = "txtBox_posY";
            this.txtBox_posY.Size = new System.Drawing.Size(100, 20);
            this.txtBox_posY.TabIndex = 7;
            // 
            // txtBox_Change
            // 
            this.txtBox_Change.Location = new System.Drawing.Point(21, 229);
            this.txtBox_Change.Name = "txtBox_Change";
            this.txtBox_Change.Size = new System.Drawing.Size(100, 20);
            this.txtBox_Change.TabIndex = 8;
            // 
            // txtBox_Start
            // 
            this.txtBox_Start.Location = new System.Drawing.Point(148, 229);
            this.txtBox_Start.Name = "txtBox_Start";
            this.txtBox_Start.Size = new System.Drawing.Size(100, 20);
            this.txtBox_Start.TabIndex = 9;
            // 
            // txtBox_Duration
            // 
            this.txtBox_Duration.Location = new System.Drawing.Point(21, 276);
            this.txtBox_Duration.Name = "txtBox_Duration";
            this.txtBox_Duration.Size = new System.Drawing.Size(100, 20);
            this.txtBox_Duration.TabIndex = 10;
            // 
            // lbl_duration
            // 
            this.lbl_duration.AutoSize = true;
            this.lbl_duration.Location = new System.Drawing.Point(18, 260);
            this.lbl_duration.Name = "lbl_duration";
            this.lbl_duration.Size = new System.Drawing.Size(47, 13);
            this.lbl_duration.TabIndex = 11;
            this.lbl_duration.Text = "Duration";
            // 
            // lbl_change
            // 
            this.lbl_change.AutoSize = true;
            this.lbl_change.Location = new System.Drawing.Point(18, 213);
            this.lbl_change.Name = "lbl_change";
            this.lbl_change.Size = new System.Drawing.Size(44, 13);
            this.lbl_change.TabIndex = 12;
            this.lbl_change.Text = "Change";
            // 
            // lbl_posX
            // 
            this.lbl_posX.AutoSize = true;
            this.lbl_posX.Location = new System.Drawing.Point(18, 171);
            this.lbl_posX.Name = "lbl_posX";
            this.lbl_posX.Size = new System.Drawing.Size(54, 13);
            this.lbl_posX.TabIndex = 13;
            this.lbl_posX.Text = "Position X";
            // 
            // lbl_posY
            // 
            this.lbl_posY.AutoSize = true;
            this.lbl_posY.Location = new System.Drawing.Point(145, 171);
            this.lbl_posY.Name = "lbl_posY";
            this.lbl_posY.Size = new System.Drawing.Size(54, 13);
            this.lbl_posY.TabIndex = 14;
            this.lbl_posY.Text = "Position Y";
            // 
            // lbl_start
            // 
            this.lbl_start.AutoSize = true;
            this.lbl_start.Location = new System.Drawing.Point(145, 213);
            this.lbl_start.Name = "lbl_start";
            this.lbl_start.Size = new System.Drawing.Size(29, 13);
            this.lbl_start.TabIndex = 15;
            this.lbl_start.Text = "Start";
            // 
            // btn_addChange
            // 
            this.btn_addChange.Location = new System.Drawing.Point(21, 141);
            this.btn_addChange.Name = "btn_addChange";
            this.btn_addChange.Size = new System.Drawing.Size(227, 23);
            this.btn_addChange.TabIndex = 16;
            this.btn_addChange.Text = "Füge eine Änderung zum Protokoll hinzu";
            this.btn_addChange.UseVisualStyleBackColor = true;
            this.btn_addChange.Click += new System.EventHandler(this.btn_addChange_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(488, 466);
            this.Controls.Add(this.btn_addChange);
            this.Controls.Add(this.lbl_start);
            this.Controls.Add(this.lbl_posY);
            this.Controls.Add(this.lbl_posX);
            this.Controls.Add(this.lbl_change);
            this.Controls.Add(this.lbl_duration);
            this.Controls.Add(this.txtBox_Duration);
            this.Controls.Add(this.txtBox_Start);
            this.Controls.Add(this.txtBox_Change);
            this.Controls.Add(this.txtBox_posY);
            this.Controls.Add(this.txtBox_posX);
            this.Controls.Add(this.lbl_test);
            this.Controls.Add(this.btn_openfile);
            this.Controls.Add(this.btn_createFile);
            this.Controls.Add(this.lbl_dateiname);
            this.Controls.Add(this.txtB_Dateiname);
            this.Name = "Form1";
            this.Text = "Excel Manipulator";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox txtB_Dateiname;
        private System.Windows.Forms.Label lbl_dateiname;
        private System.Windows.Forms.Button btn_createFile;
        private System.Windows.Forms.Button btn_openfile;
        private System.Windows.Forms.Label lbl_test;
        private System.Windows.Forms.TextBox txtBox_posX;
        private System.Windows.Forms.TextBox txtBox_posY;
        private System.Windows.Forms.TextBox txtBox_Change;
        private System.Windows.Forms.TextBox txtBox_Start;
        private System.Windows.Forms.TextBox txtBox_Duration;
        private System.Windows.Forms.Label lbl_duration;
        private System.Windows.Forms.Label lbl_change;
        private System.Windows.Forms.Label lbl_posX;
        private System.Windows.Forms.Label lbl_posY;
        private System.Windows.Forms.Label lbl_start;
        private System.Windows.Forms.Button btn_addChange;
    }
}

