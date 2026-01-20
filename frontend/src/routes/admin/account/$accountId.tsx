import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/account/$accountId')({
  component: AdminAccountDetails,
})

function AdminAccountDetails() {
  const { accountId } = Route.useParams()
  
  return (
    <div className="p-2">
      <h2>Account Details</h2>
      <p>View account details for account: {accountId}</p>
    </div>
  )
}
