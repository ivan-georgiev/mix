using System;
using System.Reflection;
using System.Windows.Forms;
using ThesesController.GUI;
using ThesesController;

namespace ThesesController.Models
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {

            //This is unused DAL layer for manual SQL coding
            //DBS mydb = new DBS("localhost","StudentsOP");
            //var res = mydb.GetCountries();
            //mydb.DisconnectDb();

            ThesisesContext db = new ThesisesContext();
            Model cnt = new Model(db);

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
           
            
            //todo check how to hande exceptions within
            try
            {
                Application.Run(new MainForm(cnt));
            }
            catch (AmbiguousMatchException)
            {
                Application.Run(new MainForm(cnt));
            }
        }
    }
}
