using System.ComponentModel.DataAnnotations.Schema;
using System.Data.Entity.ModelConfiguration;

namespace ThesesController.Models.Mapping
{
    public class EDUCATION_FORMSMap : EntityTypeConfiguration<EDUCATION_FORMS>
    {
        public EDUCATION_FORMSMap()
        {
            // Primary Key
            this.HasKey(t => t.ID);

            // Properties
            this.Property(t => t.NAME)
                .IsRequired()
                .IsFixedLength()
                .HasMaxLength(20);

            // Table & Column Mappings
            this.ToTable("EDUCATION_FORMS");
            this.Property(t => t.ID).HasColumnName("ID");
            this.Property(t => t.NAME).HasColumnName("NAME");
        }
    }
}
