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
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(488, 466);
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
    }
}

