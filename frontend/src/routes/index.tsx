import {createFileRoute} from '@tanstack/react-router'
import {useEffect, useState} from 'react'
import {Camera, CheckCircle2, ChevronRight, FileText, Menu, Upload, X, Zap} from 'lucide-react';


export const Route = createFileRoute('/')({
  component: App,
})

function App() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [scrolled, setScrolled] = useState(false);

  useEffect(() => {
    const handleScroll = () => setScrolled(window.scrollY > 20);
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const features = [
    {
      title: "Receive Instantly",
      desc: "Get invoices from vendors directly in your secure digital inbox.",
      icon: <FileText className="w-6 h-6 text-blue-600" />
    },
    {
      title: "Snap & Upload",
      desc: "Upload proof of payment or receipt images in seconds from any device.",
      icon: <Camera className="w-6 h-6 text-indigo-600" />
    },
    {
      title: "Automated Tracking",
      desc: "Our AI extracts data so you never have to manually type an invoice again.",
      icon: <Zap className="w-6 h-6 text-amber-600" />
    }
  ];

  return (
      <div className="min-h-screen bg-white text-slate-900 font-sans selection:bg-blue-100">
        {/* Navigation */}
        <nav className={`fixed w-full z-50 transition-all duration-300 ${scrolled ? 'bg-white/80 backdrop-blur-md shadow-sm py-3' : 'bg-transparent py-5'}`}>
          <div className="max-w-7xl mx-auto px-6 flex justify-between items-center">
            <div className="flex items-center gap-2">
              <div className="w-10 h-10 bg-blue-600 rounded-xl flex items-center justify-center text-white shadow-lg shadow-blue-200">
                <FileText size={24} />
              </div>
              <span className="text-xl font-bold tracking-tight">InvoiceFlow</span>
            </div>

            {/* Desktop Nav */}
            <div className="hidden md:flex items-center gap-8">
              <a href="#features" className="text-sm font-medium text-slate-600 hover:text-blue-600 transition-colors">Features</a>
              <a href="#solutions" className="text-sm font-medium text-slate-600 hover:text-blue-600 transition-colors">Solutions</a>
              <a href="#pricing" className="text-sm font-medium text-slate-600 hover:text-blue-600 transition-colors">Pricing</a>
              <button className="px-5 py-2.5 bg-blue-600 text-white rounded-full text-sm font-semibold hover:bg-blue-700 transition-all hover:shadow-lg hover:shadow-blue-200">
                Get Started
              </button>
            </div>

            {/* Mobile Toggle */}
            <button className="md:hidden" onClick={() => setIsMenuOpen(!isMenuOpen)}>
              {isMenuOpen ? <X /> : <Menu />}
            </button>
          </div>
        </nav>

        {/* Hero Section */}
        <section className="pt-32 pb-20 lg:pt-48 lg:pb-32 px-6">
          <div className="max-w-7xl mx-auto grid lg:grid-cols-2 gap-12 items-center">
            <div className="space-y-8">
              <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-blue-50 text-blue-600 text-xs font-bold tracking-wide uppercase">
              <span className="relative flex h-2 w-2">
                <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-blue-400 opacity-75"></span>
                <span className="relative inline-flex rounded-full h-2 w-2 bg-blue-600"></span>
              </span>
                New: AI Receipt Scanning
              </div>
              <h1 className="text-5xl lg:text-7xl font-extrabold leading-[1.1] tracking-tight text-slate-900">
                Invoices received. <br />
                <span className="text-blue-600 underline decoration-blue-100 underline-offset-8">Images captured.</span><br />
                Done.
              </h1>
              <p className="text-xl text-slate-500 max-w-lg leading-relaxed">
                The simplest way for businesses to manage inbound billing. Receive invoices, snap photos of receipts, and keep your accounting perfectly synced.
              </p>
              <div className="flex flex-col sm:flex-row gap-4">
                <button className="px-8 py-4 bg-blue-600 text-white rounded-2xl font-bold text-lg hover:bg-blue-700 transition-all flex items-center justify-center gap-2 group shadow-xl shadow-blue-100">
                  Get Started Free <ChevronRight className="group-hover:translate-x-1 transition-transform" />
                </button>
                <button className="px-8 py-4 bg-white border-2 border-slate-100 text-slate-600 rounded-2xl font-bold text-lg hover:bg-slate-50 transition-all">
                  View Demo
                </button>
              </div>
              <div className="flex items-center gap-4 pt-4">
                <div className="flex -space-x-3">
                  {[1,2,3,4].map(i => (
                      <div key={i} className="w-10 h-10 rounded-full border-2 border-white bg-slate-200 flex items-center justify-center text-[10px] font-bold">
                        U{i}
                      </div>
                  ))}
                </div>
                <p className="text-sm text-slate-500 font-medium">Joined by 2,000+ businesses this month</p>
              </div>
            </div>

            <div className="relative group">
              <div className="absolute -inset-4 bg-gradient-to-tr from-blue-100 to-indigo-50 rounded-[2.5rem] -z-10 blur-2xl opacity-50 group-hover:opacity-100 transition-opacity"></div>
              <div className="bg-white rounded-[2rem] border border-slate-100 shadow-2xl p-4 overflow-hidden">
                {/* Mock UI */}
                <div className="bg-slate-50 rounded-xl p-6">
                  <div className="flex justify-between items-center mb-8">
                    <h3 className="font-bold">Recent Invoices</h3>
                    <div className="flex gap-1">
                      <div className="w-2 h-2 rounded-full bg-red-400"></div>
                      <div className="w-2 h-2 rounded-full bg-amber-400"></div>
                      <div className="w-2 h-2 rounded-full bg-green-400"></div>
                    </div>
                  </div>
                  <div className="space-y-4">
                    {[
                      { vendor: 'Amazon Web Services', amount: '$420.00', status: 'Pending Image', color: 'amber' },
                      { vendor: 'Starbucks Office', amount: '$15.40', status: 'Verifying', color: 'blue' },
                      { vendor: 'Apple Inc.', amount: '$1,299.00', status: 'Complete', color: 'green' }
                    ].map((item, i) => (
                        <div key={i} className="bg-white p-4 rounded-xl shadow-sm flex items-center justify-between border border-slate-50">
                          <div className="flex items-center gap-3">
                            <div className={`w-10 h-10 rounded-lg bg-${item.color}-50 flex items-center justify-center text-${item.color}-600`}>
                              <FileText size={18} />
                            </div>
                            <div>
                              <p className="font-bold text-sm">{item.vendor}</p>
                              <p className="text-xs text-slate-400">{item.amount}</p>
                            </div>
                          </div>
                          <div className={`px-3 py-1 rounded-full text-[10px] font-bold bg-${item.color}-50 text-${item.color}-600 uppercase`}>
                            {item.status}
                          </div>
                        </div>
                    ))}
                  </div>
                  <div className="mt-8 p-8 border-2 border-dashed border-blue-200 rounded-2xl bg-blue-50/50 flex flex-col items-center justify-center gap-3 text-center cursor-pointer hover:bg-blue-50 transition-colors">
                    <div className="w-12 h-12 bg-white rounded-full shadow-sm flex items-center justify-center text-blue-600">
                      <Upload size={20} />
                    </div>
                    <p className="text-sm font-semibold text-blue-900">Upload Receipt Image</p>
                    <p className="text-xs text-blue-700/60">Drag and drop or click to browse</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* Social Proof */}
        <section className="py-12 border-y border-slate-50">
          <div className="max-w-7xl mx-auto px-6">
            <p className="text-center text-sm font-bold text-slate-400 uppercase tracking-widest mb-8 text-center">Trusted by forward-thinking teams</p>
            <div className="flex flex-wrap justify-center items-center gap-12 md:gap-24 opacity-50 grayscale hover:grayscale-0 transition-all">
              <span className="text-2xl font-black italic">Vercel</span>
              <span className="text-2xl font-black italic">Stripe</span>
              <span className="text-2xl font-black italic">Airbnb</span>
              <span className="text-2xl font-black italic">Linear</span>
            </div>
          </div>
        </section>

        {/* Features Section */}
        <section id="features" className="py-24 px-6 bg-slate-50/50">
          <div className="max-w-7xl mx-auto">
            <div className="text-center max-w-2xl mx-auto mb-20 space-y-4">
              <h2 className="text-4xl font-bold tracking-tight">Everything you need to close the loop on billing.</h2>
              <p className="text-lg text-slate-500">Stop chasing employees for receipts. InvoiceFlow automates the collection and matching process.</p>
            </div>

            <div className="grid md:grid-cols-3 gap-8">
              {features.map((feature, i) => (
                  <div key={i} className="bg-white p-8 rounded-[2rem] border border-slate-100 hover:shadow-xl transition-all duration-300">
                    <div className="w-14 h-14 bg-white shadow-sm border border-slate-50 rounded-2xl flex items-center justify-center mb-6">
                      {feature.icon}
                    </div>
                    <h3 className="text-xl font-bold mb-3">{feature.title}</h3>
                    <p className="text-slate-500 leading-relaxed">{feature.desc}</p>
                  </div>
              ))}
            </div>
          </div>
        </section>

        {/* Call to Action Banner */}
        <section className="py-20 px-6">
          <div className="max-w-7xl mx-auto bg-slate-900 rounded-[3rem] overflow-hidden relative">
            <div className="absolute top-0 right-0 w-1/2 h-full bg-blue-600/10 skew-x-12 translate-x-1/2"></div>
            <div className="relative p-12 lg:p-20 flex flex-col items-center text-center space-y-8">
              <h2 className="text-3xl lg:text-5xl font-bold text-white max-w-3xl">Ready to simplify your company spending?</h2>
              <p className="text-slate-400 text-lg max-w-xl">Join 10,000+ teams who have already saved hundreds of hours on manual invoice reconciliation.</p>
              <div className="flex flex-col sm:flex-row gap-4 w-full justify-center">
                <button className="px-10 py-4 bg-white text-slate-900 rounded-2xl font-bold hover:bg-blue-50 transition-all">
                  Get Started for Free
                </button>
                <button className="px-10 py-4 border border-slate-700 text-white rounded-2xl font-bold hover:bg-slate-800 transition-all">
                  Contact Sales
                </button>
              </div>
              <div className="flex items-center gap-6 text-slate-500 text-sm">
                <span className="flex items-center gap-1"><CheckCircle2 size={16} className="text-green-500" /> No credit card</span>
                <span className="flex items-center gap-1"><CheckCircle2 size={16} className="text-green-500" /> Cancel anytime</span>
                <span className="flex items-center gap-1"><CheckCircle2 size={16} className="text-green-500" /> 14-day free trial</span>
              </div>
            </div>
          </div>
        </section>

        {/* Footer */}
        <footer className="pt-20 pb-10 px-6 border-t border-slate-100">
          <div className="max-w-7xl mx-auto">
            <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-12 mb-16">
              <div className="col-span-2">
                <div className="flex items-center gap-2 mb-6">
                  <div className="w-8 h-8 bg-blue-600 rounded-lg flex items-center justify-center text-white">
                    <FileText size={18} />
                  </div>
                  <span className="text-lg font-bold tracking-tight">InvoiceFlow</span>
                </div>
                <p className="text-slate-500 max-w-xs mb-6">
                  The next generation of business billing and receipt management for modern teams.
                </p>
              </div>
              <div>
                <h4 className="font-bold mb-6">Product</h4>
                <ul className="space-y-4 text-sm text-slate-500">
                  <li className="hover:text-blue-600 cursor-pointer">Features</li>
                  <li className="hover:text-blue-600 cursor-pointer">Integrations</li>
                  <li className="hover:text-blue-600 cursor-pointer">Pricing</li>
                  <li className="hover:text-blue-600 cursor-pointer">Security</li>
                </ul>
              </div>
              <div>
                <h4 className="font-bold mb-6">Company</h4>
                <ul className="space-y-4 text-sm text-slate-500">
                  <li className="hover:text-blue-600 cursor-pointer">About Us</li>
                  <li className="hover:text-blue-600 cursor-pointer">Careers</li>
                  <li className="hover:text-blue-600 cursor-pointer">Blog</li>
                  <li className="hover:text-blue-600 cursor-pointer">Press</li>
                </ul>
              </div>
              <div>
                <h4 className="font-bold mb-6">Legal</h4>
                <ul className="space-y-4 text-sm text-slate-500">
                  <li className="hover:text-blue-600 cursor-pointer">Privacy</li>
                  <li className="hover:text-blue-600 cursor-pointer">Terms</li>
                  <li className="hover:text-blue-600 cursor-pointer">Cookie Policy</li>
                </ul>
              </div>
            </div>
            <div className="pt-8 border-t border-slate-100 flex flex-col md:flex-row justify-between items-center gap-4 text-slate-400 text-xs">
              <p>© 2024 InvoiceFlow Inc. All rights reserved.</p>
              <div className="flex gap-6">
                <span>Twitter</span>
                <span>LinkedIn</span>
                <span>GitHub</span>
              </div>
            </div>
          </div>
        </footer>
      </div>
  );
};