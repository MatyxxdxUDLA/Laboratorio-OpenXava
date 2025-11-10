package com.tuempresa.facturacion.calculadores;
 
import javax.persistence.*;

import org.openxava.calculators.*;
import org.openxava.jpa.*;

import lombok.*;
 
public class CalculadorSiguienteNumeroParaAnyo
    implements ICalculator { // Un calculador tiene que implementar ICalculator
 
    @Getter @Setter     
    int anyo; // Este valor se inyectar antes de calcular
 
    public Object calculate() throws Exception { // Hace el clculo
        // Consultamos Factura y Pedido por separado ya que DocumentoComercial es @MappedSuperclass
        Query queryFactura = XPersistence.getManager()
            .createQuery("select max(f.numero) from Factura f where f.anyo = :anyo");
        queryFactura.setParameter("anyo", anyo);
        Integer ultimoNumeroFactura = (Integer) queryFactura.getSingleResult();
        
        Query queryPedido = XPersistence.getManager()
            .createQuery("select max(p.numero) from Pedido p where p.anyo = :anyo");
        queryPedido.setParameter("anyo", anyo);
        Integer ultimoNumeroPedido = (Integer) queryPedido.getSingleResult();
        
        // Tomamos el máximo entre ambos
        Integer ultimoNumero = null;
        if (ultimoNumeroFactura != null && ultimoNumeroPedido != null) {
            ultimoNumero = Math.max(ultimoNumeroFactura, ultimoNumeroPedido);
        } else if (ultimoNumeroFactura != null) {
            ultimoNumero = ultimoNumeroFactura;
        } else if (ultimoNumeroPedido != null) {
            ultimoNumero = ultimoNumeroPedido;
        }
        
        return ultimoNumero == null ? 1 : ultimoNumero + 1;
    }
 
}
