using System.ComponentModel.DataAnnotations.Schema;
using System.Data.Entity.ModelConfiguration;

namespace ThesesController.Models.Mapping
{
    public class SPECIALITyMap : EntityTypeConfiguration<SPECIALITy>
    {
        public SPECIALITyMap()
        {
            // Primary Key
            this.HasKey(t => t.ID);

            // Properties
            this.Property(t => t.NAME)
                .IsRequired()
                .IsFixedLength()
                .HasMaxLength(30);

            // Table & Column Mappings
            this.ToTable("SPECIALITIES");
            this.Property(t => t.ID).HasColumnName("ID");
            this.Property(t => t.NAME).HasColumnName("NAME");
            this.Property(t => t.DEPARTAMENT_ID).HasColumnName("DEPARTAMENT_ID");
            this.Property(t => t.DEGREE_ID).HasColumnName("DEGREE_ID");

            // Relationships
            this.HasRequired(t => t.DEGREE)
                .WithMany(t => t.SPECIALITIES)
                .HasForeignKey(d => d.DEGREE_ID);
            this.HasRequired(t => t.DEPARTAMENT)
                .WithMany(t => t.SPECIALITIES)
                .HasForeignKey(d => d.DEPARTAMENT_ID);

        }
    }
}
