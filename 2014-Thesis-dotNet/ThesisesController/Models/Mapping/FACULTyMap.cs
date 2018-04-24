using System.ComponentModel.DataAnnotations.Schema;
using System.Data.Entity.ModelConfiguration;

namespace ThesesController.Models.Mapping
{
    public class FACULTyMap : EntityTypeConfiguration<FACULTy>
    {
        public FACULTyMap()
        {
            // Primary Key
            this.HasKey(t => t.ID);

            // Properties
            this.Property(t => t.NAME)
                .IsRequired()
                .IsFixedLength()
                .HasMaxLength(30);

            this.Property(t => t.DESCRIPTION)
                .IsFixedLength()
                .HasMaxLength(100);

            // Table & Column Mappings
            this.ToTable("FACULTIES");
            this.Property(t => t.ID).HasColumnName("ID");
            this.Property(t => t.HEAD_ID).HasColumnName("HEAD_ID");
            this.Property(t => t.NAME).HasColumnName("NAME");
            this.Property(t => t.DESCRIPTION).HasColumnName("DESCRIPTION");

            // Relationships
            this.HasOptional(t => t.OFFICIAL)
                .WithMany(t => t.FACULTIES)
                .HasForeignKey(d => d.HEAD_ID);

        }
    }
}
