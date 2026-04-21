export function formatFilter(query, filterColumn, indexFilter, valueFilter, compare) {
  query.page = 0

  // Si genero un fichero de filtros, filterColumn pasa a ser un array filterColumns e indexFilter es el "indice" de columna que está filtrando
  query.filtros.push({
    campo: filterColumn.key, // id, nombre, descripcion ...
    valor: Array.isArray(valueFilter) ? valueFilter.join() : valueFilter,
    tipo: filterColumn.tipoDato, // long, string, boolean, date ...
    expresion: filterColumn.expresionFiltrado, // CONTAINS, EQUALS, BETWEEN, IN ...
    comparación: compare, // gte, lt, gt, lte ...
  })

  var filtro = {}

  query.filtros = query.filtros.reverse().filter((current) => {
    var exists = !filtro[current.campo] || false
    filtro[current.campo] = true
    return exists
  })
}
