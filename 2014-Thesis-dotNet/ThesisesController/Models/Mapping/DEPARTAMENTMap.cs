using System.ComponentModel.DataAnnotations.Schema;
using System.Data.Entity.ModelConfiguration;

namespace ThesesController.Models.Mapping
{
    public class DEPARTAMENTMap : EntityTypeConfiguration<DEPARTAMENT>
    {
        public DEPARTAMENTMap()
        {
            // Primary Key
            this.HasKey(t => t.ID);

            // Properties
            this.Property(t => t.NAME)
                .IsRequired()
                .IsFixedLength()
                .HasMaxLength(40);

            this.Property(t => t.DESRIPTION)
                .IsFixedLength()
                .HasMaxLength(100);

            // Table & Column Mappings
            this.ToTable("DEPARTAMENTS");
            this.Property(t => t.ID).HasColumnName("ID");
            this.Property(t => t.FACULTY_ID).HasColumnName("FACULTY_ID");
            this.Property(t => t.NAME).HasColumnName("NAME");
            this.Property(t => t.HEAD_ID).HasColumnName("HEAD_ID");
            this.Property(t => t.DESRIPTION).HasColumnName("DESRIPTION");

            // Relationships
            this.HasRequired(t => t.FACULTy)
                .WithMany(t => t.DEPARTAMENTS)
                .HasForeignKey(d => d.FACULTY_ID);
            this.HasOptional(t => t.OFFICIAL)
                .WithMany(t => t.DEPARTAMENTS)
                .HasForeignKey(d => d.HEAD_ID);

        }
    }
}
