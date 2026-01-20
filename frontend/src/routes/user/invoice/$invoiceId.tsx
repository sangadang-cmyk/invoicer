import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/user/invoice/$invoiceId')({
  component: UserInvoiceDetails,
})

function UserInvoiceDetails() {
  const { invoiceId } = Route.useParams()
  
  return (
    <div className="p-2">
      <h2>Invoice Details</h2>
      <p>View invoice details for invoice: {invoiceId}</p>
    </div>
  )
}
