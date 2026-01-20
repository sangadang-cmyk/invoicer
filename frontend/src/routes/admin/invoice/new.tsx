import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/invoice/new')({
  component: AdminInvoiceNew,
})

function AdminInvoiceNew() {
  return (
    <div className="p-2">
      <h2>Create New Invoice</h2>
      <p>Create new invoice placeholder</p>
    </div>
  )
}
