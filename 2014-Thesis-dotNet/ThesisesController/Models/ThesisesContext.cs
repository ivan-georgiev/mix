using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using ThesesController.Models.Mapping;

namespace ThesesController.Models
{
    public partial class ThesisesContext : DbContext
    {
        static ThesisesContext()
        {
            Database.SetInitializer<ThesisesContext>(null);
        }

        public ThesisesContext()
            : base("Name=ThesisesContext")
        {
        }

        public DbSet<COUNTRy> COUNTRIES { get; set; }
        public DbSet<DEGREE> DEGREES { get; set; }
        public DbSet<DEPARTAMENT> DEPARTAMENTS { get; set; }
        public DbSet<DOCUMENT_TYPES> DOCUMENT_TYPES { get; set; }
        public DbSet<DOCUMENT> DOCUMENTS { get; set; }
        public DbSet<EDUCATION_FORMS> EDUCATION_FORMS { get; set; }
        public DbSet<FACULTy> FACULTIES { get; set; }
        public DbSet<KEYW_THESIS_ASSOCIACIONS> KEYW_THESIS_ASSOCIACIONS { get; set; }
        public DbSet<KEYWORD> KEYWORDS { get; set; }
        public DbSet<OFFICIAL> OFFICIALS { get; set; }
        public DbSet<PASSWORD> PASSWORDS { get; set; }
        public DbSet<POSITION> POSITIONS { get; set; }
        public DbSet<SPECIALITy> SPECIALITIES { get; set; }
        public DbSet<sysdiagram> sysdiagrams { get; set; }
        public DbSet<THESIS> THESISES { get; set; }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            modelBuilder.Configurations.Add(new COUNTRyMap());
            modelBuilder.Configurations.Add(new DEGREEMap());
            modelBuilder.Configurations.Add(new DEPARTAMENTMap());
            modelBuilder.Configurations.Add(new DOCUMENT_TYPESMap());
            modelBuilder.Configurations.Add(new DOCUMENTMap());
            modelBuilder.Configurations.Add(new EDUCATION_FORMSMap());
            modelBuilder.Configurations.Add(new FACULTyMap());
            modelBuilder.Configurations.Add(new KEYW_THESIS_ASSOCIACIONSMap());
            modelBuilder.Configurations.Add(new KEYWORDMap());
            modelBuilder.Configurations.Add(new OFFICIALMap());
            modelBuilder.Configurations.Add(new PASSWORDMap());
            modelBuilder.Configurations.Add(new POSITIONMap());
            modelBuilder.Configurations.Add(new SPECIALITyMap());
            modelBuilder.Configurations.Add(new sysdiagramMap());
            modelBuilder.Configurations.Add(new THESISMap());
        }
    }
}
