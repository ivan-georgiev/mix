using System.ComponentModel.DataAnnotations.Schema;
using System.Data.Entity.ModelConfiguration;

namespace ThesesController.Models.Mapping
{
    public class OFFICIALMap : EntityTypeConfiguration<OFFICIAL>
    {
        public OFFICIALMap()
        {
            // Primary Key
            this.HasKey(t => t.ID);

            // Properties
            this.Property(t => t.FIRST_NAME)
                .IsRequired()
                .IsFixedLength()
                .HasMaxLength(20);

            this.Property(t => t.LAST_NAME)
                .IsRequired()
                .IsFixedLength()
                .HasMaxLength(20);

            this.Property(t => t.EMAIL)
                .IsFixedLength()
                .HasMaxLength(20);

            // Table & Column Mappings
            this.ToTable("OFFICIALS");
            this.Property(t => t.ID).HasColumnName("ID");
            this.Property(t => t.FIRST_NAME).HasColumnName("FIRST_NAME");
            this.Property(t => t.LAST_NAME).HasColumnName("LAST_NAME");
            this.Property(t => t.POSITION_ID).HasColumnName("POSITION_ID");
            this.Property(t => t.DEPARTAMENT_ID).HasColumnName("DEPARTAMENT_ID");
            this.Property(t => t.EMAIL).HasColumnName("EMAIL");

            // Relationships
            this.HasRequired(t => t.DEPARTAMENT)
                .WithMany(t => t.OFFICIALS)
                .HasForeignKey(d => d.DEPARTAMENT_ID);
            this.HasRequired(t => t.POSITION)
                .WithMany(t => t.OFFICIALS)
                .HasForeignKey(d => d.POSITION_ID);

        }
    }
}
