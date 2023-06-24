import React, { useMemo } from 'react'
import { useTable, useSortBy } from 'react-table'

const ResponseTable = ({tableData, headers}) => {

  const data = useMemo(() => tableData,[tableData])

  const columns  =  useMemo(() => headers.map(header => ({Header:header.label, accessor:header.key})),[headers])

  const tableInstance = useTable({ columns, data }, useSortBy)

  const {
    getTableProps,
    getTableBodyProps,
    headerGroups,
    rows,
    prepareRow,
  } = tableInstance

  return (
    <div className='mt-5 table-responsive'>
      <table {...getTableProps()} className='table table-bordered border-dark'>
      <thead>
        {headerGroups.map(headerGroup => (
          <tr {...headerGroup.getHeaderGroupProps()}>
            {headerGroup.headers.map(column => (
              <th {...column.getHeaderProps(column.getSortByToggleProps())}>
                {column.render('Header')}
              </th>
            ))}
          </tr>
        ))}
      </thead>
      <tbody {...getTableBodyProps()}>
        {rows.map(row => {
          prepareRow(row)
          return (
            <tr {...row.getRowProps()}>
              {row.cells.map(cell => {
                return (
                  <td {...cell.getCellProps()}>
                    {cell.render('Cell')}
                  </td>
                )
              })}
            </tr>
          )
        })}
      </tbody>
      </table>
    </div>
    
  )

}

export default ResponseTable